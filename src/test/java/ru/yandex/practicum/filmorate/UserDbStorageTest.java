package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.db.UserDbStorage;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class UserDbStorageTest {
    private final JdbcTemplate jdbcTemplate;
    private UserDbStorage userDbStorage;
    private User user;

    @BeforeEach
    void set() {
        userDbStorage = new UserDbStorage(jdbcTemplate);

        user = new User(0, "petr@rambler.ru", "petr222", "Petr Serg",
                LocalDate.of(1986, 4, 4));
    }

    @Test
    @DirtiesContext
    public void testFindUserById() {
        set();
        userDbStorage.create(user);

        User savedUser = userDbStorage.userById(1);

        assertThat(savedUser)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(user);
    }

    @Test
    @DirtiesContext
    public void testFindAll() {
        set();
        userDbStorage.create(user);

        List<User> savedUsers = new ArrayList<>(userDbStorage.findAll());

        assertThat(savedUsers.size())
                .isEqualTo(1);
    }

    @Test
    @DirtiesContext
    public void testCreate() {
        set();

        User newUser = userDbStorage.create(user);

        assertThat(newUser.getId())
                .isEqualTo(1);
    }

    @Test
    @DirtiesContext
    public void testUpdate() {
        set();
        userDbStorage.create(user);
        user.setName("No name");

        userDbStorage.update(user);

        assertThat(userDbStorage.userById(1).getName())
                .isEqualTo("No name");
    }
}
