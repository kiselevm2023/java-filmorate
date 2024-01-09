package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmLikesStorage {
    void addLikeByFilmId(Integer filmId, Integer userId);

    void deleteLikeByFilmId(Integer filmId, Integer userId);

    List<Film> topFilms(Integer count);
}