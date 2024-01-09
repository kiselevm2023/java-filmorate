package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import org.springframework.web.bind.annotation.PathVariable;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmLikesStorage;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.validation.FilmValidation;

import javax.validation.Valid;
import java.util.Collection;
import java.util.List;

@Service
public class FilmService {

    private final FilmStorage filmStorage;
    private final UserService userService;
    private final FilmLikesStorage filmLikesStorage;

    @Autowired
    public FilmService(@Qualifier("filmDbStorage") FilmStorage filmStorage, UserService userService,
                       FilmLikesStorage filmLikesStorage) {
        this.filmStorage = filmStorage;
        this.userService = userService;
        this.filmLikesStorage = filmLikesStorage;
    }

    public Film addNewFilm(@Valid @RequestBody Film film) {
        FilmValidation.validate(film);
        return filmStorage.create(film);
    }

    public Film updateFilm(@Valid @RequestBody Film film) {
        try {
            FilmValidation.validate(film);
            return filmStorage.update(film);
        } catch (NotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Film is not founded", e);
        }
    }

    public Collection<Film> findAll() {
        try {
            return filmStorage.findAll();
        } catch (NotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Film is not founded", e);
        }
    }

    public Film findById(@PathVariable("id") Integer id) {
        try {
            return filmStorage.filmById(id);
        } catch (NotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Film is not founded", e);
        }
    }

    public void addNewLike(Integer filmId, Integer userId) {
        try {
            filmLikesStorage.addLikeByFilmId(filmId, userId);
        } catch (NotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    public void deleteLike(Integer filmId, Integer userId) {
        try {
            Film film = filmStorage.filmById(filmId);
            userService.findById(userId);
            filmLikesStorage.deleteLikeByFilmId(filmId, userId);
        } catch (NotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    public List<Film> findTheMostPopulars(Integer count) {
        return filmLikesStorage.topFilms(count);
    }
}
