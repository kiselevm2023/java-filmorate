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
            log.info("Validation is failed");
            throw new ValidationException("The name cannot be empty");
        }
        if (film.getDescription().length() > 200) {
            log.info("Validation is failed");
            throw new ValidationException("Description cannot be longer than 200 characters");
        }
        if (film.getReleaseDate().isBefore(date)) {
            log.info("Validation is failed");
            throw new ValidationException("The date cannot be earlier than December 28, 1985");
        }
        if (film.getDuration() <= 0) {
            log.info("Validation is failed");
            throw new ValidationException("The duration of the film must be positive");
        }
    }
}