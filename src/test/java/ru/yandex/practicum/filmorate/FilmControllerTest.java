package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.film.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;

public class FilmControllerTest {

    FilmController filmController;
    FilmStorage filmStorage;
    UserStorage userStorage;
    FilmService filmService;

    @BeforeEach
    void init() {
        filmStorage = new InMemoryFilmStorage();
        userStorage = new InMemoryUserStorage();
        filmService = new FilmService(filmStorage, userStorage);
        filmController = new FilmController(filmService);
    }

    @Test
    void shouldCreateFilm() {
        Film film = new Film(1,"Terminator 2", "It is considered one of the best science fiction, action, and sequel films ever made",
                LocalDate.of(1991,07,03),137, new HashSet<>());
        filmController.addNewFilm(film);
        Collection<Film> films = filmController.findAll();
        assertNotNull(film, "Movie list is empty");
        assertEquals(1, films.size());
    }

    @Test
    void shouldNotCreateFilmWithEmptyName() {
        Film film = new Film(2, "", "It is considered one of the best science fiction, action, and sequel films ever made",
                LocalDate.of(1991,07,03),137, new HashSet<>());
        Collection<Film> films = filmController.findAll();
        assertThrows(ValidationException.class, () -> filmController.addNewFilm(film));
        assertEquals(0, films.size());
    }

    @Test
    void shouldNotCreateFilmWithLongDescription() {
        Film film = new Film(3,"Terminator 2", "It is considered one of the best science fiction, action, and sequel films ever made." +
                "It is also seen as a major influence on visual effects in films, " +
                "helping usher in the transition from practical effects to " +
                "reliance on computer-generated imagery.", LocalDate.of(1991,07,03),137, new HashSet<>());
        Collection<Film> films = filmController.findAll();
        assertThrows(ValidationException.class, () -> filmController.addNewFilm(film));
        assertEquals(0, films.size());
    }

    @Test
    void shouldNotCreateFilmWithBadReleaseDate() {
        Film film = new Film(4,"Terminator 2", "It is considered one of the best science fiction, action, and sequel films ever made",
                LocalDate.of(1809,07,03),137, new HashSet<>());
        Collection<Film> films = filmController.findAll();
        assertThrows(ValidationException.class, () -> filmController.addNewFilm(film));
        assertEquals(0, films.size());
    }

    @Test
    void shouldNotCreateFilmWithBadDuration() {
        Film film = new Film(5, "Terminator 2","It is considered one of the best science fiction, action, and sequel films ever made",
                LocalDate.of(1991,07,03),-23, new HashSet<>());
        Collection<Film> films = filmController.findAll();
        assertThrows(ValidationException.class, () -> filmController.addNewFilm(film));
        assertEquals(0, films.size());
    }

    @Test
    void shouldUpdateFilm() {
        Film film = new Film(6,"Terminator 2", "It is considered one of the best science fiction, action, and sequel films ever made",
                LocalDate.of(1991,07,03),137, new HashSet<>());
        filmController.addNewFilm(film);
        film.setName("RoboCop");
        filmController.updateFilm(film);
        Collection<Film> films = filmController.findAll();
        assertEquals(1, films.size());
    }

    @Test
    void shouldUpdateUnknown() {
        Film film = new Film(7,"Terminator 2", "It is considered one of the best science fiction, action, and sequel films ever made",
                LocalDate.of(1991,07,03),137, new HashSet<>());
        film.setId(9999);
        assertThrows(ValidationException.class, () -> filmController.updateFilm(film));
    }

    @Test
    void shouldUpdateUserGetAll() {
        Film film = new Film(8,"Terminator 2", "It is considered one of the best science fiction, action, and sequel films ever made",
                LocalDate.of(1991,07,03),137, new HashSet<>());
        filmController.addNewFilm(film);
        Film film2 = new Film(9,"RoboCop", "It is science fiction action",
                LocalDate.of(1987,07,17),99, new HashSet<>());
        filmController.addNewFilm(film2);
        Collection<Film> films = filmController.findAll();
        assertEquals(2, films.size());
    }
}