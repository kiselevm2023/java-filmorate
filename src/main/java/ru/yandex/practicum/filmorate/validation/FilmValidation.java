package ru.yandex.practicum.filmorate.validation;

import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

@Slf4j
public class FilmValidation {

    private static final LocalDate date = LocalDate.of(1895, 12, 28);

    public static void validate(Film film) {
        if (film.getName().isEmpty()) {
            log.info("Валидация не пройдена");
            throw new ValidationException("Имя не может быть пустым");
        }
        if (film.getDescription().length() > 200) {
            log.info("Валидация не пройдена");
            throw new ValidationException("Описание не может быть длиннее 200 символов");
        }
        if (film.getReleaseDate().isBefore(date)) {
            log.info("Валидация не пройдена");
            throw new ValidationException("Дата не может быть раньше 28 декабря 1985 года");
        }
        if (film.getDuration() <= 0) {
            log.info("Валидация не пройдена");
            throw new ValidationException("Длительность фильма должна быть положительной");
        }
    }
}