package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

public class FilmControllerTest {

    FilmController filmController;

    @BeforeEach
    void init() {
        filmController = new FilmController();
    }

    @Test
    void shouldCreateFilm() { //Корректные данные
        Film film = new Film(1,"Mad Max", "Post-apocalyptic action drama",
                LocalDate.of(1979,05,12),93);
        filmController.addNewFilm(film);
        Collection<Film> films = filmController.findAll();
        assertNotNull(film, "Список фильмов пуст.");
        assertEquals(1, films.size());
    }

    @Test
    void shouldNotCreateFilmWithEmptyName() { //Проверка на пустое название фильма
        Film film = new Film(2, "", "Post-apocalyptic action drama",
                LocalDate.of(1979,05,12),93);
        Collection<Film> films = filmController.findAll();
        assertThrows(ValidationException.class, () -> filmController.addNewFilm(film));
        assertEquals(0, films.size());
    }

    @Test
    void shouldNotCreateFilmWithLongDescription() { //Проверка на описание фильма свыше 200 символов
        Film film = new Film(3,"Mad Max", "In the near future, after a major catastrophe " +
                "that struck our urban civilization, all life was concentrated along countless highways. " +
                "The road has become a way of existence. And she gave many people the opportunity " +
                "to show their most cruel instincts.", LocalDate.of(1979,05,12),93);
        Collection<Film> films = filmController.findAll();
        assertThrows(ValidationException.class, () -> filmController.addNewFilm(film));
        assertEquals(0, films.size());
    }

    @Test
    void shouldNotCreateFilmWithBadReleaseDate() { //Проверка на дату публикации фильма
        Film film = new Film(4,"Mad Max", "Post-apocalyptic action drama",
                LocalDate.of(1879,01,11),93);
        Collection<Film> films = filmController.findAll();
        assertThrows(ValidationException.class, () -> filmController.addNewFilm(film));
        assertEquals(0, films.size());
    }

    @Test
    void shouldNotCreateFilmWithBadDuration() { //Проверка на отрицательное продолжительность фильма
        Film film = new Film(5, "Mad Max","Post-apocalyptic action drama",
                LocalDate.of(1979,05,12),-93);
        Collection<Film> films = filmController.findAll();
        assertThrows(ValidationException.class, () -> filmController.addNewFilm(film));
        assertEquals(0, films.size());
    }

    @Test
    void shouldUpdateFilm() { //Обновляем фильм
        Film film = new Film(6,"Mad Max", "Post-apocalyptic action drama",
                LocalDate.of(1979,05,12),93);
        filmController.addNewFilm(film);
        film.setName("Mad Max 2: The Road Warrior");
        filmController.updateFilm(film);
        Collection<Film> films = filmController.findAll();
        assertEquals(1, films.size());
    }

    @Test
    void shouldUpdateUnknown() { //проверка на несуществующий фильм
        Film film = new Film(7,"Mad Max", "Post-apocalyptic action drama",
                LocalDate.of(1979,05,12),93);
        film.setId(9999);
        assertThrows(ValidationException.class, () -> filmController.updateFilm(film));
    }

    @Test
    void shouldUpdateUserGetAll() { //Получаем все фильмы
        Film film = new Film(8,"Mad Max", "Post-apocalyptic action drama",
                LocalDate.of(1979,05,12),93);
        filmController.addNewFilm(film);
        Film film2 = new Film(9,"Mad Max 2: The Road Warrior", "Post - apocalyptic action movie",
                LocalDate.of(1981,12,24),91);
        filmController.addNewFilm(film2);
        Collection<Film> films = filmController.findAll();
        assertEquals(2, films.size());
    }
}