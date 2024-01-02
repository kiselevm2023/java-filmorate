package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;

public class UserControllerTest {

        UserController userController;
        UserStorage userStorage;
        UserService userService;

    @BeforeEach
    void init() {
        userStorage = new InMemoryUserStorage();
        userService = new UserService(userStorage);
        userController = new UserController(userService);
    }

    @Test
    void shouldCreateUser() {
        User user = new User(1,"sergei@yandex.ru", "sergei454", "sergei",
                LocalDate.of(1990, 07, 14), new HashSet<>());
        userController.createUser(user);
        Collection<User> users = userController.findAll();
        assertNotNull(users, "Список пользователей пуст.");
        assertEquals(1, users.size());
    }

    @Test
    void shouldNotCreateUserWithEmptyEmail() {
        User user = new User(2, "","sergei454", "sergei",
                LocalDate.of(1990, 07, 14), new HashSet<>());
        Collection<User> users = userController.findAll();
        assertThrows(ValidationException.class, () -> userController.createUser(user));
        assertEquals(0, users.size());
    }

    @Test
    void shouldNotCreateUserWithBadEmail() {
        User user = new User(3,"sergei  /yandex.ru", "sergei454", "sergei",
                LocalDate.of(1997, 04, 12), new HashSet<>());
        Collection<User> users = userController.findAll();
        assertThrows(ValidationException.class, () -> userController.createUser(user));
        assertEquals(0, users.size());
    }

    @Test
    void shouldNotCreateUserWithEmptyLogin() {
        User user = new User(4, "sergei@yandex.ru", "", "sergei",
                LocalDate.of(1995, 01, 12), new HashSet<>());
        Collection<User> users = userController.findAll();
        assertThrows(ValidationException.class, () -> userController.createUser(user));
        assertEquals(0, users.size());
    }

    @Test
    void shouldNotCreateUserWithBadLogin() {
        User user = new User(5,"sergei@yandex.ru", "sergei 1 2 3 4", "sergei",
                LocalDate.of(1989, 02, 10), new HashSet<>());
        Collection<User> users = userController.findAll();
        assertThrows(ValidationException.class, () -> userController.createUser(user));
        assertEquals(0, users.size());
    }

    @Test
    void shouldCreateUserWithEmptyName() {
        User user = new User(6,"sergei@yandex.ru", "sergei454", null,
                LocalDate.of(1996, 07, 20), new HashSet<>());
        Collection<User> users = userController.findAll();
        assertEquals(0, users.size());
    }

    @Test
    void shouldNotCreateUserWithBadBirthday() {
        User user = new User(7,"sergei@yandex.ru", "sergei454", "sergei",
                LocalDate.of(2044, 11, 20), new HashSet<>());
        Collection<User> users = userController.findAll();
        assertThrows(ValidationException.class, () -> userController.createUser(user));
        assertEquals(0, users.size());
    }

    @Test
    void shouldUpdateUser() {
        User user = new User(8,"sergei@yandex.ru", "sergei454", "sergei",
                LocalDate.of(1999, 06, 29), new HashSet<>());
        userController.createUser(user);
        user.setEmail("leonid@gmail.com");
        userController.updateUser(user);
        Collection<User> users = userController.findAll();
        assertEquals(1, users.size());
    }

    @Test
    void shouldUpdateUnknown() {
        User user = new User(9,"sergei@gmail.com", "sergei454", "sergei",
                LocalDate.of(1996,06,20), new HashSet<>());
        user.setId(9999);
        assertThrows(NotFoundException.class, () -> userController.updateUser(user));
    }

    @Test
    void shouldUpdateUserGetAll() {
        User user = new User(10,"sergei@yandex.ru", "sergei454", "sergei",
                LocalDate.of(1996, 06, 20), new HashSet<>());
        userController.createUser(user);
        User user2 = new User(11,"mickail@yandex.ru", "mickail1597", "mickail",
                LocalDate.of(1990, 07, 22), new HashSet<>());
        userController.createUser(user2);
        Collection<User> users = userController.findAll();
        assertEquals(2, users.size());
    }
}
