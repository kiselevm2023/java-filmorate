package ru.yandex.practicum.filmorate.storage.db;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmLikesStorage;

import java.util.List;

@Component
public class FilmLikesDbStorage implements FilmLikesStorage {
    private final JdbcTemplate jdbcTemplate;
    private final FilmDbStorage filmDbStorage;
    private static final String INSERT_LIKE_BY_USER = "INSERT INTO likes VALUES(?, ?)";
    private static final String DELETE_LIKE_FOR_FILM = "DELETE FROM likes WHERE film_id = ? AND user_id = ?;";
    private static final String GET_FILMS_BY_LIKES = "SELECT count(user_id) AS quantity, f.*, r.rating_name " +
            "FROM likes l " +
            "RIGHT JOIN films f ON f.film_id = l.film_id  " +
            "JOIN ratings r ON r.rating_id = f.rating_id " +
            "GROUP BY f.film_id, r.rating_id " +
            "ORDER BY quantity DESC " +
            "LIMIT ?";

    @Autowired
    public FilmLikesDbStorage(JdbcTemplate jdbcTemplate, FilmDbStorage filmDbStorage) {
        this.jdbcTemplate = jdbcTemplate;
        this.filmDbStorage = filmDbStorage;
    }

    @Override
    public void addLikeByFilmId(Integer filmId, Integer userId) {
        jdbcTemplate.update(INSERT_LIKE_BY_USER, filmId, userId);
    }

    @Override
    public void deleteLikeByFilmId(Integer filmId, Integer userId) {
        jdbcTemplate.update(DELETE_LIKE_FOR_FILM, filmId, userId);
    }

    @Override
    public List<Film> topFilms(Integer count) {
        return jdbcTemplate.query(GET_FILMS_BY_LIKES, filmDbStorage.filmRowMapper(), count);
    }
}