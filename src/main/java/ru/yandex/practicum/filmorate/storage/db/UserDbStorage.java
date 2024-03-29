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
import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class UserDbStorage implements UserStorage {
    private final JdbcTemplate jdbcTemplate;

    private static final String GET_ALL_USERS = "SELECT * FROM users;";
    private static final String GET_USER_BY_ID = "SELECT * FROM users WHERE user_id = ?;";
    private static final String UPDATE_USER = "UPDATE users SET " +
            "name = ?, " +
            "login = ?, " +
            "email = ?, " +
            "birthday = ? " +
            "WHERE user_id = ?;";

    @Autowired
    public UserDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<User> findAll() {
        List<User> users = jdbcTemplate.query(GET_ALL_USERS, userRowMapper());
        log.debug("A list of users was obtained from the database:" + users);
        return users;
    }

    @Override
    public User findUserById(Integer userId) {
        try {
            User user = jdbcTemplate.queryForObject(GET_USER_BY_ID, userRowMapper(), userId);
            log.debug("A user was retrieved from the database with ID=" + userId);
            return user;
        } catch (RuntimeException e) {
            log.warn("User is not founded with ID=" + userId);
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
        log.debug("A user has been added to the database: " + user);
        return user;
    }

    @Override
    public User update(User user) {
        if (findUserById(user.getId()) == null) {
            log.warn("User is not founded with ID=" + user.getId());
            throw new NotFoundException(String.format("The user with id = %d is not founded.", user.getId()));
        }

        jdbcTemplate.update(UPDATE_USER, user.getName(), user.getLogin(), user.getEmail(),
                user.getBirthday(), user.getId());
        log.debug("User data has been updated in the database with ID=" + user.getId());
        return findUserById(user.getId());
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
