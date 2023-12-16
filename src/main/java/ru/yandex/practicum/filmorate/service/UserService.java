package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;
import ru.yandex.practicum.filmorate.validation.UserValidation;

import java.util.Collection;
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
        User user = userStorage.findById(userId);
        User friend = userStorage.findById(friendId);
        user.setFriends(friendId);
        friend.setFriends(userId);

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
        return userStorage.findById(id);
    }

    public void deleteFriend(Integer userId, Integer friendId) {
        User user = userStorage.findById(userId);
        User friend = userStorage.findById(friendId);
        user.deleteFriend(friendId);
        friend.deleteFriend(userId);
    }

    public Collection<User> getUsersFriends(Integer id) {
        User user = userStorage.findById(id);
        return user.getFriends()
                .stream()
                .map(userStorage::findById)
                .collect(Collectors.toList());
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
