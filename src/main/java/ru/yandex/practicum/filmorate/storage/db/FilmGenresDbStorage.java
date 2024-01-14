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

    private static final String queryAllGenresByFilm = "SELECT fg.genre_id, g.genre_name " +
            "FROM films_genres fg " +
            "JOIN genres g ON g.genre_id = fg.genre_id " +
            "WHERE film_id = ?";
    private static final String queryInsertGenresByFilm = "INSERT INTO films_genres (genre_id, film_id) VALUES (?, ?);";
    private static final String queryDeleteGenresByFilm = "DELETE FROM films_genres WHERE film_id = ?";

    @Autowired
    public FilmGenresDbStorage(JdbcTemplate jdbcTemplate, GenreDbStorage genreDbStorage) {
        this.jdbcTemplate = jdbcTemplate;
        this.genreDbStorage = genreDbStorage;
    }

    @Override
    public Collection<Genre> getGenresByFilmId(Integer filmId) {
        Collection<Genre> filmGenres = jdbcTemplate.query(queryAllGenresByFilm, genreDbStorage.genreRowMapper(), filmId);
        log.warn("Film with id=" + filmId + " \n" + "has a list of genres in the database: " + filmGenres);
        return filmGenres;
    }

    @Override
    public void addGenres(Film film) {
        log.warn("Adding a list of movie genres to the database: " + film);

        List<Genre> genres = new ArrayList<>(film.getGenres());

        jdbcTemplate.batchUpdate(queryInsertGenresByFilm, new BatchPreparedStatementSetter() {
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
        jdbcTemplate.update(queryDeleteGenresByFilm, film.getId());
        addGenres(film);
        log.debug("Updating the list of film genres in the database: " + film);
    }
}