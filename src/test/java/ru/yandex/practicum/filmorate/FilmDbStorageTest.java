package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.db.FilmDbStorage;
import ru.yandex.practicum.filmorate.storage.db.FilmGenresDbStorage;
import ru.yandex.practicum.filmorate.storage.db.GenreDbStorage;


import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class FilmDbStorageTest {
    private final JdbcTemplate jdbcTemplate;
    private FilmDbStorage filmDbStorage;
    private Film film;

    @Autowired
    void set() {
        GenreDbStorage genreDbStorage = new GenreDbStorage(jdbcTemplate);
        FilmGenresDbStorage filmGenresDbStorage = new FilmGenresDbStorage(jdbcTemplate, genreDbStorage);
        filmDbStorage = new FilmDbStorage(jdbcTemplate, filmGenresDbStorage);

        film = new Film(0, "Taxi Driver", "Neo-noir psychological thriller film",
                LocalDate.of(1976, 2, 9), 114, new Mpa(4, "R"));
    }

    @Test
    @DirtiesContext
    void shouldReturnListSizeFilmWith1() {
        filmDbStorage.create(film);

        List<Film> savedFilms = new ArrayList<>(filmDbStorage.findAll());

        assertThat(savedFilms.size())
                .isEqualTo(1);
    }

    @Test
    @DirtiesContext
    void shouldReturnFilmWithId1() {
        filmDbStorage.create(film);

        Film savedFilm = filmDbStorage.findFilmById(1);

        assertThat(savedFilm)
                .isNotNull()
                .isEqualTo(film);
    }

    @Test
    @DirtiesContext
    void shouldCreateFilmWithId1() {
        Film savedFilm = filmDbStorage.create(film);

        assertThat(savedFilm.getId())
                .isEqualTo(1);
    }

    @Test
    @DirtiesContext
    void shouldReturnNameNoNameFilm() {
        filmDbStorage.create(film);
        film.setName("No movie name");

        filmDbStorage.update(film);

        assertThat(filmDbStorage.findFilmById(1).getName())
                .isEqualTo("No movie name");
    }
}