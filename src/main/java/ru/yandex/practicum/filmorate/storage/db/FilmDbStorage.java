package ru.yandex.practicum.filmorate.storage.db;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class FilmDbStorage implements FilmStorage {
    private final JdbcTemplate jdbcTemplate;
    private final FilmGenresDbStorage filmGenresDbStorage;

    private static final String GET_ALL_FILMS_WITH_RATINGS = "SELECT * FROM films f JOIN ratings r ON r.rating_id = f.rating_id ORDER BY film_id;";
    private static final String GET_FILM_BY_ID = "SELECT * FROM films f JOIN ratings r ON r.rating_id = f.rating_id WHERE film_id = ?;";
    private static final String UPDATE_FILM = "UPDATE films SET " +
            "name = ?, " +
            "description  = ?, " +
            "release_date  = ?, " +
            "duration  = ?, " +
            "rating_id = ? " +
            "WHERE film_id = ?;";


    @Autowired
    public FilmDbStorage(JdbcTemplate jdbcTemplate, FilmGenresDbStorage filmGenresDbStorage) {
        this.jdbcTemplate = jdbcTemplate;
        this.filmGenresDbStorage = filmGenresDbStorage;
    }

    @Override
    public List<Film> findAll() {
        return jdbcTemplate.query(GET_ALL_FILMS_WITH_RATINGS, filmRowMapper());
    }

    @Override
    public Film findFilmById(Integer filmId) {
        try {
            return jdbcTemplate.queryForObject(GET_FILM_BY_ID, filmRowMapper(), filmId);
        } catch (RuntimeException e) {
            log.warn("The film is not founded with  ID=" + filmId);
            throw new NotFoundException(String.format("The film with id = %d is not founded.", filmId));
        }
    }

    @Override
    public Film create(Film film) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate.getDataSource())
                .withTableName("films")
                .usingGeneratedKeyColumns("film_id");

        Map<String, Object> params = Map.of(
                "name", film.getName(),
                "description", film.getDescription(),
                "release_date", film.getReleaseDate().toString(),
                "duration", film.getDuration(),
                "rating_id", film.getMpa().getId()
        );
        Number id = simpleJdbcInsert.executeAndReturnKey(params);
        film.setId(id.intValue());

        if (film.getGenres().size() != 0) {
            filmGenresDbStorage.addGenres(film);
        }
        log.debug("В БД добавлен фильм: " + film);
        return film;
    }

    @Override
    public Film update(Film film) {
        if (findFilmById(film.getId()) == null) {
            log.warn("The film is not founded with ID=" + film.getId());
            throw new NotFoundException(String.format("The film with id = %d is not founded.", film.getId()));
        }

        jdbcTemplate.update(UPDATE_FILM, film.getName(), film.getDescription(), film.getReleaseDate(), film.getDuration(),
                film.getMpa().getId(), film.getId());

        filmGenresDbStorage.updateGenres(film);
        log.debug("The movie has been updated in the database: " + film);
        return findFilmById(film.getId());
    }

    protected RowMapper<Film> filmRowMapper() {
        return new RowMapper<Film>() {
            @Override
            public Film mapRow(ResultSet rs, int rowNum) throws SQLException {

                Film film = new Film(
                        rs.getInt("film_id"),
                        rs.getString("name"),
                        rs.getString("description"),
                        rs.getDate("release_date").toLocalDate(),
                        rs.getInt("duration"),
                        new Mpa(rs.getInt("rating_id"), rs.getString("rating_name"))
                );
                film.getGenres().addAll(filmGenresDbStorage.getGenresByFilmId(film.getId()));
                return film;
            }
        };
    }
}