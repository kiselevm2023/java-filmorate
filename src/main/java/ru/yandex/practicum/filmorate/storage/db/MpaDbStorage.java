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

    @Autowired
    public MpaDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Collection<Mpa> findAll() {
        String sql = "SELECT * FROM ratings;";
        return jdbcTemplate.query(sql, mpaRowMapper());
    }

    @Override
    public Mpa mpaById(Integer mpaId) {
        try {
            String sql = "SELECT * FROM ratings WHERE rating_id = ?;";
            return jdbcTemplate.queryForObject(sql, mpaRowMapper(), mpaId);
        } catch (RuntimeException e) {
            log.warn("Не найден mpa с ID=" + mpaId);
            //throw new EntityNotFoundException("Не найден mpa с ID=" + mpaId);
            throw new NotFoundException(String.format("The film with id = %d is not founded.", mpaId));
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