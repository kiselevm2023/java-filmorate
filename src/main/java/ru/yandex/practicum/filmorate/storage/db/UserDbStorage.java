package ru.yandex.practicum.filmorate.storage.db;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Map;

@Component
@Slf4j
public class UserDbStorage implements UserStorage {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public UserDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Collection<User> findAll() {
        String sql = "SELECT * FROM users;";
        Collection<User> users = jdbcTemplate.query(sql, userRowMapper());
        log.debug("Из БД получен список пользователей:" + users);
        return users;
    }

    @Override
    public User userById(Integer userId) {
        try {
            String sql = "SELECT * FROM users WHERE user_id = ?;";
            User user = jdbcTemplate.queryForObject(sql, userRowMapper(), userId);
            log.debug("Из БД получен пользователь с ID=" + userId);
            return user;
        } catch (RuntimeException e) {
            log.warn("Не найден пользователь с ID=" + userId);
            throw new NotFoundException(String.format("The user with id = %d is not founded.", userId));
        }
    }

    @Override
    public User create(User user) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate.getDataSource())
                .withTableName("users")
                .usingGeneratedKeyColumns("user_id");
        Map<String, String> params = Map.of(
                "email", user.getEmail(),
                "login", user.getLogin(),
                "name", user.getName(),
                "birthday", user.getBirthday().toString()
        );
        Number id = simpleJdbcInsert.executeAndReturnKey(params);
        user.setId(id.intValue());
        log.debug("В БД добавлен пользователь: " + user);
        return user;
    }

    @Override
    public User update(User user) {
        if (userById(user.getId()) == null) {
            log.warn("Не найден пользователь с ID=" + user.getId());
            //throw new UserNotFoundException("Не найден пользователь с ID=" + user.getId());
            throw new NotFoundException(String.format("The user with id = %d is not founded.", user.getId()));
        }

        String sql = "UPDATE users SET " +
                "name = ?, " +
                "login = ?, " +
                "email = ?, " +
                "birthday = ? " +
                "WHERE user_id = ?;";

        jdbcTemplate.update(sql, user.getName(), user.getLogin(), user.getEmail(),
                user.getBirthday(), user.getId());
        log.debug("В БД обновлены данные пользователя с ID=" + user.getId());
        return userById(user.getId());
    }

    protected RowMapper<User> userRowMapper() {
        return new RowMapper<User>() {
            @Override
            public User mapRow(ResultSet rs, int rowNum) throws SQLException {
                User user = new User(
                        rs.getInt("user_id"),
                        rs.getString("email"),
                        rs.getString("login"),
                        rs.getString("name"),
                        rs.getDate("birthday").toLocalDate()
                );
                return user;
            }
        };
    }
}
