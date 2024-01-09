package ru.yandex.practicum.filmorate.storage.db;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.FilmGenresStorage;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Component
@Slf4j
public class FilmGenresDbStorage implements FilmGenresStorage {
    private final JdbcTemplate jdbcTemplate;
    private final GenreDbStorage genreDbStorage;

    @Autowired
    public FilmGenresDbStorage(JdbcTemplate jdbcTemplate, GenreDbStorage genreDbStorage) {
        this.jdbcTemplate = jdbcTemplate;
        this.genreDbStorage = genreDbStorage;
    }

    @Override
    public Collection<Genre> getGenresByFilmId(Integer filmId) {
        String sql = "SELECT fg.genre_id, g.genre_name " +
                "FROM films_genres fg " +
                "JOIN genres g ON g.genre_id = fg.genre_id " +
                "WHERE film_id = ?";
        Collection<Genre> filmGenres = jdbcTemplate.query(sql, genreDbStorage.genreRowMapper(), filmId);
        log.warn("Фильм с id=" + filmId + " имеет в БД список жанров: " + filmGenres);
        return filmGenres;
    }

    @Override
    public void addGenres(Film film) {
        log.warn("Добавление в БД список жанров фильма: " + film);

        List<Genre> genres = new ArrayList<>(film.getGenres());
        String sql = "INSERT INTO films_genres (genre_id, film_id) VALUES (?, ?);";

        jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(@NonNull PreparedStatement ps, int i) throws SQLException {
                ps.setInt(1, genres.get(i).getId());
                ps.setInt(2, film.getId());
            }

            @Override
            public int getBatchSize() {
                return genres.size();
            }
        });
    }

    @Override
    public void updateGenres(Film film) {
        String sqlDelete = "DELETE FROM films_genres WHERE film_id = ?";
        jdbcTemplate.update(sqlDelete, film.getId());
        addGenres(film);
        log.debug("Обновление в БД списка жанров фильма: " + film);
    }
}