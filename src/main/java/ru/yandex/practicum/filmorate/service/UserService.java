package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import ru.yandex.practicum.filmorate.storage.FriendsStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;
import ru.yandex.practicum.filmorate.validation.UserValidation;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserStorage userStorage;
    private final FriendsStorage friendsStorage;

    @Autowired
    public UserService(@Qualifier("userDbStorage") UserStorage userStorage, FriendsStorage friendsStorage) {
        this.userStorage = userStorage;
        this.friendsStorage = friendsStorage;
    }

    public void addNewFriend(Integer userId, Integer friendId) {
        try {
            friendsStorage.addFriend(userId, friendId);
        } catch (NotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User is not founded", e);
        }
    }

    public Collection<User> findAll() {
        return userStorage.findAll();
    }

    public User createUser(User user) {
        UserValidation.validate(user);
        return userStorage.create(user);
    }

    public User updateUser(User user) {
        UserValidation.validate(user);
        return userStorage.update(user);
    }

    public User findById(Integer id) {
        try {
            return userStorage.userById(id);
        } catch (NotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User is not founded", e);
        }
    }

    public void deleteFriend(Integer userId, Integer friendId) {
        friendsStorage.deleteFriend(userId, friendId);
    }

    public Collection<User> getUsersFriends(Integer id) {
        try {
            return friendsStorage.getFriendsByUserId(id);
        } catch (NotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User is not founded", e);
        }
    }

    public Collection<User> findCommonFriends(int userId, int otherId) {
        return friendsStorage.commonFriends(userId, otherId);
    }
}
