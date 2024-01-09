package ru.yandex.practicum.filmorate.storage.db;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FriendsStorage;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
@Slf4j
public class FriendsDbStorage implements FriendsStorage {
    private final JdbcTemplate jdbcTemplate;
    private final UserDbStorage userDbStorage;

    @Autowired
    public FriendsDbStorage(JdbcTemplate jdbcTemplate, UserDbStorage userDbStorage) {
        this.jdbcTemplate = jdbcTemplate;
        this.userDbStorage = userDbStorage;
    }

    @Override
    public Collection<User> getFriendsByUserId(Integer userId) {
        String sql = "select * from users where user_id in (select friend_id from friends where user_id = ?);";
        return jdbcTemplate.query(sql, userDbStorage.userRowMapper(), userId);
    }

    @Override
    public void addFriend(Integer userId, Integer friendId) {
        try {
            String sql = "INSERT INTO friends VALUES(?,?);";
            jdbcTemplate.update(sql, userId, friendId);
        } catch (RuntimeException e) {
            log.warn("User is not founded with ID=" + friendId);
            throw new NotFoundException(String.format("The friend with friendId = %d is not founded.", friendId));
        }
    }

    @Override
    public void deleteFriend(Integer userId, Integer friendId) {
        String sql = "DELETE FROM friends WHERE user_id = ? AND friend_id = ?;";
        jdbcTemplate.update(sql, userId, friendId);
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
