package ru.yandex.practicum.filmorate.storage.db;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.GenreStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Component
@Slf4j
public class GenreDbStorage implements GenreStorage {
    private final JdbcTemplate jdbcTemplate;

    private static final String GET_ALL_GENRES = "SELECT * FROM genres;";
    private static final String GET_GENRE_BY_ID = "SELECT * FROM genres WHERE genre_id = ?;";

    @Autowired
    public GenreDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Genre> findAll() {
        return jdbcTemplate.query(GET_ALL_GENRES, genreRowMapper());
    }

    @Override
    public Genre getGenreById(Integer genreId) {
        try {
            return jdbcTemplate.queryForObject(GET_GENRE_BY_ID, genreRowMapper(), genreId);
        } catch (RuntimeException e) {
            log.warn("Genre is not founded with ID=" + genreId);
            throw new NotFoundException(String.format("The genre with id = %d is not founded.", genreId));
        }
    }

    protected RowMapper<Genre> genreRowMapper() {
        return new RowMapper<Genre>() {
            @Override
            public Genre mapRow(ResultSet rs, int rowNum) throws SQLException {
                return new Genre(
                        rs.getInt("genre_id"),
                        rs.getString("genre_name")
                );
            }
        };
    }
}