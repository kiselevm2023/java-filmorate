package ru.yandex.practicum.filmorate.storage.db;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FriendsStorage;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
@Slf4j
public class FriendsDbStorage implements FriendsStorage {
    private final JdbcTemplate jdbcTemplate;
    private final UserDbStorage userDbStorage;

    private static final String GET_FRIENDS_BY_USER_ID = "select * from users where user_id in (select friend_id from friends where user_id = ?);";
    private static final String INSERT_FRIEND_BY_USER_ID = "INSERT INTO friends VALUES(?,?);";
    private static final String DELETE_FRIEND_BY_USER_ID = "DELETE FROM friends WHERE user_id = ? AND friend_id = ?;";

    @Autowired
    public FriendsDbStorage(JdbcTemplate jdbcTemplate, UserDbStorage userDbStorage) {
        this.jdbcTemplate = jdbcTemplate;
        this.userDbStorage = userDbStorage;
    }

    @Override
    public List<User> getFriendsByUserId(Integer userId) {
        return jdbcTemplate.query(GET_FRIENDS_BY_USER_ID, userDbStorage.userRowMapper(), userId);
    }

    @Override
    public void addFriend(Integer userId, Integer friendId) {
        try {
            jdbcTemplate.update(INSERT_FRIEND_BY_USER_ID, userId, friendId);
        } catch (RuntimeException e) {
            log.warn("User is not founded with ID=" + friendId);
            throw new NotFoundException(String.format("The friend with friendId = %d is not founded.", friendId));
        }
    }

    @Override
    public void deleteFriend(Integer userId, Integer friendId) {
        jdbcTemplate.update(DELETE_FRIEND_BY_USER_ID, userId, friendId);
    }

    @Override
    public List<User> commonFriends(Integer userId, Integer otherId) {
        Set<User> users = new HashSet<>(getFriendsByUserId(userId));
        Set<User> otherIds = new HashSet<>(getFriendsByUserId(otherId));
        List<User> commonFriends = new ArrayList<>();
        for (User user : otherIds) {
            if (users.contains(user)) {
                commonFriends.add(user);
            }
        }
        log.debug("List of mutual friends of the user with id=" + userId + " and user with id=" + otherId);
        return commonFriends;
    }
}
