package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.Collection;

public interface FilmGenresStorage {
    Collection<Genre> getGenresByFilmId(Integer filmId);

    void addGenres(Film film);

    void updateGenres(Film film);
}