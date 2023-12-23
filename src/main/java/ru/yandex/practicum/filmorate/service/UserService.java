package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;
import ru.yandex.practicum.filmorate.validation.UserValidation;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserService {

    private UserStorage userStorage;

    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public void addNewFriend(Integer userId, Integer friendId) {
        try {
            User user = userStorage.findById(userId);
            User friend = userStorage.findById(friendId);
            user.setFriends(friendId);
            friend.setFriends(userId);
        } catch (NotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User is not founded", e);
        }
    }

    public Collection<User> findAll() {
        return userStorage.findAll();
    }

    public User createUser(User user) {
        UserValidation.validate(user);
        return userStorage.createUser(user);
    }

    public User updateUser(User user) {
        UserValidation.validate(user);
        return userStorage.updateUser(user);
    }

    public User findById(Integer id) {
        try {
            return userStorage.findById(id);
        } catch (NotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User is not founded", e);
        }
    }

    public void deleteFriend(Integer userId, Integer friendId) {
        User user = userStorage.findById(userId);
        User friend = userStorage.findById(friendId);
        user.deleteFriend(friendId);
        friend.deleteFriend(userId);
    }

    public List<User> getUsersFriends(Integer id) {
        try {
            User user = userStorage.findById(id);
            return user.getFriends()
                    .stream()
                    .map(userStorage::findById)
                    .collect(Collectors.toList());
        } catch (NotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User is not founded", e);
        }
    }

    public Collection<User> findCommonFriends(int userId, int otherUserId) {
        User user = userStorage.findById(userId);
        User otherUser = userStorage.findById(otherUserId);
        Set<Integer> userFriends = user.getFriends();
        Set<Integer> otherUserFriends = otherUser.getFriends();

        return userFriends.stream()
                .filter(otherUserFriends::contains)
                .map(userStorage::findById)
                .collect(Collectors.toList());
    }
}
