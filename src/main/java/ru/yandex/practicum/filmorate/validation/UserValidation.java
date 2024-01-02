package ru.yandex.practicum.filmorate.validation;

import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

@Slf4j
public class UserValidation {

    public static User validate(User user) {
        if (user.getEmail().isEmpty() || !user.getEmail().contains("@")) {
            log.info("Validation is failed");
            throw new ValidationException("Mail must contain the symbol \"@\" and not be empty");
        }
        if (user.getLogin().isEmpty() || user.getLogin().contains(" ")) {
            log.info("Validation is failed");
            throw new ValidationException("Login cannot be empty and must not contain spaces");
        }
        if (user.getName() == null || user.getName().isEmpty()) {
            user.setName(user.getLogin());
        }
        if (user.getBirthday().isAfter(LocalDate.now())) {
            log.info("Validation is failed");
            throw new ValidationException("Date of birth cannot be in the future");
        }
        return user;
    }

}