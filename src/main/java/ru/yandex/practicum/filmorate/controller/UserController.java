package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.validation.UserValidation;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {

    private final Map<Integer, User> users = new HashMap<>();
    private int generatorId = 1;

    @GetMapping
    public Collection<User> findAll() { //получение списка всех пользователей.
        return users.values();
    }

    @PostMapping
    public User createUser(@RequestBody User user) { //имя для отображения может быть пустым — в таком случае будет использован логин;
        log.info("Creating user {}", user);
        UserValidation.validate(user);
        user.setId(generatorId++);
        users.put(user.getId(), user);
        return user;
    }

    @PutMapping
    public User updateUser(@Valid @RequestBody User user) { //обновление пользователя;
        log.info("Updating user {}", user);
        UserValidation.validate(user);
        if (!users.containsKey(user.getId())) {
            throw new ValidationException("Пользователя с данным id не существует");
        }
        users.put(user.getId(), user);
        return user;
    }
}

