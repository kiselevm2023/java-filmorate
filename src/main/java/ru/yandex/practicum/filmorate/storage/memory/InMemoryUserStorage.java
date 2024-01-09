package ru.yandex.practicum.filmorate.storage.memory;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Component
public class InMemoryUserStorage implements UserStorage {

    private final Map<Integer, User> users = new HashMap<>();
    private Integer generatorId = 0;

    @Override
    public Collection<User> findAll() {
        return users.values();
    }

    @Override
    public User create(User user) {
        user.setId(++generatorId);
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public User update(User user) {
        if (users.containsKey(user.getId())) {
            users.put(user.getId(), user);
        } else {
            throw new NotFoundException("The user with this id does not exist");
        }
        return user;
    }

    @Override
    public User userById(Integer id) {
        if (users.containsKey(id)) {
            return users.get(id);
        } else {
            throw new NotFoundException(String.format("The user with id = %d is not founded.", id));
        }
    }


    public void deleteById(Integer id) {
        if (users.containsKey(id)) {
            users.remove(id);
        } else {
            throw new NotFoundException(String.format("The user with id = %d is not founded.", id));
        }
    }
}