package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.FilmLikesStorage;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.FriendsStorage;
import ru.yandex.practicum.filmorate.storage.memory.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.memory.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.time.LocalDate;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

public class FilmControllerTest {

    FilmController filmController;
    FilmStorage filmStorage;
    UserStorage userStorage;
    FriendsStorage friendsStorage;
    FilmService filmService;
    UserService userService;
    FilmLikesStorage filmLikesStorage;

    @BeforeEach
    void init() {
        filmStorage = new InMemoryFilmStorage();
        userStorage = new InMemoryUserStorage();
        userService = new UserService(userStorage, friendsStorage);
        filmService = new FilmService(filmStorage, userService, filmLikesStorage);
        filmController = new FilmController(filmService);
    }

    @Test
    void shouldCreateFilm() {
        Film film = Film.builder()
                .name("Terminator 2")
                .description("It is considered one of the best science fiction, action, and sequel films ever made")
                .releaseDate(LocalDate.of(1991, 07, 03))
                .duration(137)
                .build();
        filmController.addNewFilm(film);
        Collection<Film> films = filmController.findAll();
        assertNotNull(film, "Movie list is empty");
        assertEquals(1, films.size());
    }

    @Test
    void shouldNotCreateFilmWithEmptyName() {
        Film film2 = Film.builder()
                .name("")
                .description("It is considered one of the best science fiction, action, and sequel films ever made")
                .releaseDate(LocalDate.of(1991, 07, 03))
                .duration(137)
                .build();
        Collection<Film> films = filmController.findAll();
        assertThrows(ValidationException.class, () -> filmController.addNewFilm(film2));
        assertEquals(0, films.size());
    }

    @Test
    void shouldNotCreateFilmWithLongDescription() {
        Film film3 = Film.builder()
                .name("Terminator 2")
                .description("It is considered one of the best science fiction, action, and sequel films ever made." +
                        "It is also seen as a major influence on visual effects in films, " +
                        "helping usher in the transition from practical effects to " +
                        "reliance on computer-generated imagery.")
                .releaseDate(LocalDate.of(1991, 07, 03))
                .duration(137)
                .build();
        Collection<Film> films = filmController.findAll();
        assertThrows(ValidationException.class, () -> filmController.addNewFilm(film3));
        assertEquals(0, films.size());
    }

    @Test
    void shouldNotCreateFilmWithBadReleaseDate() {
        Film film4 = Film.builder()
                .name("Terminator 2")
                .description("It is considered one of the best science fiction, action, and sequel films ever made")
                .releaseDate(LocalDate.of(1809, 07, 03))
                .duration(137)
                .build();
        Collection<Film> films = filmController.findAll();
        assertThrows(ValidationException.class, () -> filmController.addNewFilm(film4));
        assertEquals(0, films.size());
    }

    @Test
    void shouldNotCreateFilmWithBadDuration() {
        Film film5 = Film.builder()
                .name("Terminator 2")
                .description("It is considered one of the best science fiction, action, and sequel films ever made")
                .releaseDate(LocalDate.of(1991, 07, 03))
                .duration(-23)
                .build();

        Collection<Film> films = filmController.findAll();
        assertThrows(ValidationException.class, () -> filmController.addNewFilm(film5));
        assertEquals(0, films.size());
    }

    @Test
    void shouldUpdateFilm() {
        Film film6 = Film.builder()
                .name("Terminator 2")
                .description("It is considered one of the best science fiction, action, and sequel films ever made")
                .releaseDate(LocalDate.of(1991, 07, 03))
                .duration(137)
                .build();

        filmController.addNewFilm(film6);
        film6.setName("RoboCop");
        filmController.updateFilm(film6);
        Collection<Film> films = filmController.findAll();
        assertEquals(1, films.size());
    }

    @Test
    void shouldUpdateUnknown() {
        Film film7 = Film.builder()
                .name("Terminator 2")
                .description("It is considered one of the best science fiction, action, and sequel films ever made")
                .releaseDate(LocalDate.of(1991, 07, 03))
                .duration(137)
                .build();

        film7.setId(9999);
        assertThrows(ValidationException.class, () -> filmController.updateFilm(film7));
    }

    @Test
    void shouldUpdateUserGetAll() {
        Film film8 = Film.builder()
                .name("RoboCop")
                .description("It is science fiction action")
                .releaseDate(LocalDate.of(1987, 07, 17))
                .duration(99)
                .build();

        filmController.addNewFilm(film8);
        Film film9 = Film.builder()
                .name("RoboCop")
                .description("It is science fiction action")
                .releaseDate(LocalDate.of(1987, 07, 17))
                .duration(99)
                .build();

        filmController.addNewFilm(film9);
        Collection<Film> films = filmController.findAll();
        assertEquals(2, films.size());
    }
}