package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
//import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.FriendsStorage;
import ru.yandex.practicum.filmorate.storage.memory.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.time.LocalDate;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

public class UserControllerTest {

        UserController userController;
        UserStorage userStorage;
        UserService userService;
        FriendsStorage friendsStorage;

    @BeforeEach
    void init() {
        userStorage = new InMemoryUserStorage();
        userService = new UserService(userStorage, friendsStorage);
        userController = new UserController(userService);
    }

    /*User user = User.builder()
            .email("sergei@yandex.ru")
            .login("sergei454")
            .name("sergei")
            .birthday(LocalDate.of(1990, 07, 14))
            .build(); */

    @Test
    void shouldCreateUser() {
        /*User user = new User(1,"sergei@yandex.ru", "sergei454", "sergei",
                LocalDate.of(1990, 07, 14)); */
        User user = User.builder()
                .email("sergei@yandex.ru")
                .login("sergei454")
                .name("sergei")
                .birthday(LocalDate.of(1990, 07, 14))
                .build();
        userController.createUser(user);
        Collection<User> users = userController.findAll();
        assertNotNull(users, "Список пользователей пуст.");
        assertEquals(1, users.size());
    }

    @Test
    void shouldNotCreateUserWithEmptyEmail() {
        /*User user = new User(2, "","sergei454", "sergei",
                LocalDate.of(1990, 07, 14), new HashSet<>()); */
        User user2 = User.builder()
                .email("")
                .login("sergei454")
                .name("sergei")
                .birthday(LocalDate.of(1990, 07, 14))
                .build();
        Collection<User> users = userController.findAll();
        assertThrows(ValidationException.class, () -> userController.createUser(user2));
        assertEquals(0, users.size());
    }

    @Test
    void shouldNotCreateUserWithBadEmail() {
        /*User user = new User(3,"sergei  /yandex.ru", "sergei454", "sergei",
                LocalDate.of(1997, 04, 12), new HashSet<>()); */
        User user3 = User.builder()
                .email("sergei  /yandex.ru")
                .login("sergei454")
                .name("sergei")
                .birthday(LocalDate.of(1990, 07, 14))
                .build();
        Collection<User> users = userController.findAll();
        assertThrows(ValidationException.class, () -> userController.createUser(user3));
        assertEquals(0, users.size());
    }

    @Test
    void shouldNotCreateUserWithEmptyLogin() {
        /*User user = new User(4, "sergei@yandex.ru", "", "sergei",
                LocalDate.of(1995, 01, 12), new HashSet<>()); */
        User user4 = User.builder()
                .email("sergei@yandex.ru")
                .login("")
                .name("sergei")
                .birthday(LocalDate.of(1990, 07, 14))
                .build();
        Collection<User> users = userController.findAll();
        assertThrows(ValidationException.class, () -> userController.createUser(user4));
        assertEquals(0, users.size());
    }

    @Test
    void shouldNotCreateUserWithBadLogin() {
        /*User user = new User(5,"sergei@yandex.ru", "sergei 1 2 3 4", "sergei",
                LocalDate.of(1989, 02, 10), new HashSet<>()); */
        User user5 = User.builder()
                .email("sergei@yandex.ru")
                .login("sergei 1 2 3 4")
                .name("sergei")
                .birthday(LocalDate.of(1990, 07, 14))
                .build();
        Collection<User> users = userController.findAll();
        assertThrows(ValidationException.class, () -> userController.createUser(user5));
        assertEquals(0, users.size());
    }

    @Test
    void shouldCreateUserWithEmptyName() {
        /*User user = new User(6,"sergei@yandex.ru", "sergei454", null,
                LocalDate.of(1996, 07, 20), new HashSet<>()); */
        User user6 = User.builder()
                .email("sergei@yandex.ru")
                .login("sergei454")
                .name(null)
                .birthday(LocalDate.of(1990, 07, 14))
                .build();
        Collection<User> users = userController.findAll();
        assertEquals(0, users.size());
    }

    @Test
    void shouldNotCreateUserWithBadBirthday() {
        /*User user = new User(7,"sergei@yandex.ru", "sergei454", "sergei",
                LocalDate.of(2044, 11, 20), new HashSet<>()); */
        User user7 = User.builder()
                .email("sergei@yandex.ru")
                .login("sergei454")
                .name("sergei")
                .birthday(LocalDate.of(2044, 11, 20))
                .build();
        Collection<User> users = userController.findAll();
        assertThrows(ValidationException.class, () -> userController.createUser(user7));
        assertEquals(0, users.size());
    }

    @Test
    void shouldUpdateUser() {
        /* User user = new User(8,"sergei@yandex.ru", "sergei454", "sergei",
                LocalDate.of(1999, 06, 29), new HashSet<>()); */
        User user8 = User.builder()
                .email("sergei@yandex.ru")
                .login("sergei454")
                .name("sergei")
                .birthday(LocalDate.of(1990, 11, 20))
                .build();
        userController.createUser(user8);
        user8.setEmail("leonid@gmail.com");
        userController.updateUser(user8);
        Collection<User> users = userController.findAll();
        assertEquals(1, users.size());
    }

    @Test
    void shouldUpdateUnknown() {
        /*User user = new User(9,"sergei@gmail.com", "sergei454", "sergei",
                LocalDate.of(1996,06,20), new HashSet<>()); */
        User user9 = User.builder()
                .email("sergei@yandex.ru")
                .login("sergei454")
                .name("sergei")
                .birthday(LocalDate.of(1990, 11, 20))
                .build();
        user9.setId(9999);
        assertThrows(NotFoundException.class, () -> userController.updateUser(user9));
    }

    @Test
    void shouldUpdateUserGetAll() {
        /*User user = new User(10,"sergei@yandex.ru", "sergei454", "sergei",
                LocalDate.of(1996, 06, 20), new HashSet<>()); */
        User user10 = User.builder()
                .email("sergei@yandex.ru")
                .login("sergei454")
                .name("sergei")
                .birthday(LocalDate.of(1996, 06, 20))
                .build();
        userController.createUser(user10);
        /*User user2 = new User(11,"mickail@yandex.ru", "mickail1597", "mickail",
                LocalDate.of(1990, 07, 22), new HashSet<>()); */
        User user11 = User.builder()
                .email("mickail@yandex.ru")
                .login("mickail1597")
                .name("mickail")
                .birthday(LocalDate.of(1990, 07, 22))
                .build();
        userController.createUser(user11);
        Collection<User> users = userController.findAll();
        assertEquals(2, users.size());
    }
}