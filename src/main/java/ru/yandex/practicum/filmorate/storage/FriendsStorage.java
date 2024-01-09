package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.List;

public interface FriendsStorage {
    Collection<User> getFriendsByUserId(Integer userId);

    void addFriend(Integer userId, Integer friendId);

    void deleteFriend(Integer userId, Integer friendId);

    List<User> commonFriends(Integer userId, Integer otherId);
}