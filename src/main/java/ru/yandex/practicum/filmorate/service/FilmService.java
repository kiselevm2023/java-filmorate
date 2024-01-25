package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmLikesStorage;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.validation.FilmValidation;

import javax.validation.Valid;
import java.util.List;
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

    public Film addNewFilm(@Valid Film film) {
        FilmValidation.validate(film);
        return filmStorage.create(film);
    }

    public Film updateFilm(@Valid Film film) {
        try {
            FilmValidation.validate(film);
            return filmStorage.update(film);
        } catch (NotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Film is not founded", e);
        }
    }

    public List<Film> findAll() {
        try {
            return filmStorage.findAll();
        } catch (NotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Film is not founded", e);
        }
    }

    public Film findById(Integer id) {
        try {
            return filmStorage.findFilmById(id);
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
            Film film = filmStorage.findFilmById(filmId);
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
