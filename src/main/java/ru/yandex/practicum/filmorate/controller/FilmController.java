package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.validation.FilmValidation;

import javax.validation.Valid;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;


@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {

    private final Map<Integer, Film> films = new HashMap<>();
    private Integer generatorId = 1;

    @GetMapping
    public Collection<Film> findAll() { //возращает коллекцию всех фильмов
        return films.values();
    }

    @PostMapping
    public Film addNewFilm(@Valid @RequestBody Film film) { //добавляем фильм
        log.info("Creating film {}", film);
        FilmValidation.validate(film);
        film.setId(generatorId++);
        films.put(film.getId(), film);
        return film;
    }

    @PutMapping
    public Film updateFilm(@Valid @RequestBody Film film) { //обновляем фильм
        log.info("Updating film {}", film);
        FilmValidation.validate(film);
        if (!films.containsKey(film.getId())) {
            throw new ValidationException("Фильма с таким id не существует.");
        }
        films.put(film.getId(), film);
        return film;
    }
}

