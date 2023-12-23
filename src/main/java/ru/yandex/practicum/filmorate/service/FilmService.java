package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import org.springframework.web.bind.annotation.PathVariable;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;
import ru.yandex.practicum.filmorate.validation.FilmValidation;

import javax.validation.Valid;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class FilmService {

    private FilmStorage filmStorage;
    private UserStorage userStorage;

    public Film addNewFilm(@Valid @RequestBody Film film) {
        FilmValidation.validate(film);
        return filmStorage.addNewFilm(film);
    }

    public Film updateFilm(@Valid @RequestBody Film film) {
        FilmValidation.validate(film);
        return filmStorage.updateFilm(film);
    }

    public Collection<Film> findAll() {
        return filmStorage.findAll();
    }

    public Film findById(@PathVariable("id") Integer id) {
        try {
            return filmStorage.findById(id);
        } catch (NotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Film is not founded", e);
        }
    }

    @Autowired
    public FilmService(FilmStorage filmStorage, UserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    public void addNewLike(Integer userId, Integer filmId) {
        try {
            Film film = filmStorage.findById(filmId);
            userStorage.findById(userId);
            film.setLikes(filmId);
        } catch (NotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    public void deleteLike(Integer userId, Integer filmId) {
        try {
            Film film = filmStorage.findById(filmId);
            userStorage.findById(userId);
            film.deleteLike(filmId);
        } catch (NotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    public List<Film> findTheMostPopulars(Integer count) {
        return filmStorage.findAll()
                .stream()
                .sorted((film1, film2) -> film2.getLikes().size() - film1.getLikes().size())
                .limit(count)
                .collect(Collectors.toList());
    }
}
