package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;
import ru.yandex.practicum.filmorate.validation.FilmValidation;

import javax.validation.Valid;
import java.util.Collection;
import java.util.stream.Collectors;


@Service
public class FilmService {

    private FilmStorage filmStorage;
    private UserStorage userStorage;

    @PostMapping
    public Film addNewFilm(@Valid @RequestBody Film film) {
        FilmValidation.validate(film);
        return filmStorage.addNewFilm(film);
    }

    @PutMapping
    public Film updateFilm(@Valid @RequestBody Film film) {
        FilmValidation.validate(film);
        return filmStorage.updateFilm(film);
    }

    @GetMapping
    public Collection<Film> findAll() {
        return filmStorage.findAll();
    }

    @GetMapping("/{id}")
    public Film findById(@PathVariable("id") Integer id) {
        return filmStorage.findById(id);
    }

    @Autowired
    public FilmService(FilmStorage filmStorage, UserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    public void addNewLike(Integer userId, Integer filmId) {
        Film film = filmStorage.findById(filmId);
        userStorage.findById(userId);
        film.setLikes(filmId);
    }

    public void deleteLike(Integer userId, Integer filmId) {
        Film film = filmStorage.findById(filmId);
        userStorage.findById(userId);
        film.deleteLike(filmId);
    }

    public Collection<Film> findTheMostPopulars(Integer count) {
        return filmStorage.findAll()
                .stream()
                .sorted((film1, film2) -> film2.getLikes().size() - film1.getLikes().size())
                .limit(count)
                .collect(Collectors.toList());
    }
}
