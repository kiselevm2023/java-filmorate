package ru.yandex.practicum.filmorate.validation;

import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

@Slf4j
public class UserValidation {

    public static User validate(User user) {
        if (user.getEmail().isEmpty() || !user.getEmail().contains("@")) {
            log.info("Валидация не пройдена");
            throw new ValidationException("Почта должна содержать символ \"@\" и не быть пустым");
        }
        if (user.getLogin().isEmpty() || user.getLogin().contains(" ")) {
            log.info("Валидация не пройдена");
            throw new ValidationException("Логин не может быть пустым и не должен содержать пробелы");
        }
        if (user.getName() == null || user.getName().isEmpty()) {
            user.setName(user.getLogin());
        }
        if (user.getBirthday().isAfter(LocalDate.now())) {
            log.info("Валидация не пройдена");
            throw new ValidationException("Дата рождения не может быть в будущем");
        }
        return user;
    }

}