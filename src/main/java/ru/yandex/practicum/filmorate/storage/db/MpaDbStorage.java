package ru.yandex.practicum.filmorate.storage.db;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.MpaStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;

@Component
@Slf4j
public class MpaDbStorage implements MpaStorage {
    private final JdbcTemplate jdbcTemplate;

    private static final String queryAllMpa = "SELECT * FROM ratings;";
    private static final String queryMpaById = "SELECT * FROM ratings WHERE rating_id = ?;";

    @Autowired
    public MpaDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Collection<Mpa> findAll() {
        return jdbcTemplate.query(queryAllMpa, mpaRowMapper());
    }

    @Override
    public Mpa mpaById(Integer mpaId) {
        try {
            return jdbcTemplate.queryForObject(queryMpaById, mpaRowMapper(), mpaId);
        } catch (RuntimeException e) {
            log.warn("Mpa is not founded with ID=" + mpaId);
            throw new NotFoundException(String.format("The rating with id = %d is not founded.", mpaId));
        }
    }

    private RowMapper<Mpa> mpaRowMapper() {
        return new RowMapper<Mpa>() {
            @Override
            public Mpa mapRow(ResultSet rs, int rowNum) throws SQLException {
                return new Mpa(
                        rs.getInt("rating_id"),
                        rs.getString("rating_name")
                );
            }
        };
    }
}