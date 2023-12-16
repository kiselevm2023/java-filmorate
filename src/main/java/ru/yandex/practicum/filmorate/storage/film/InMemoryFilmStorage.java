package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Component
public class InMemoryFilmStorage implements FilmStorage {

    private final Map<Integer, Film> films = new HashMap<>();
    private Integer generatorId = 0;

    @Override
    public Film addNewFilm(Film film) {
        film.setId(++generatorId);
        films.put(film.getId(), film);
        return film;
    }

    @Override
    public Film updateFilm(Film film) {
        if (films.containsKey(film.getId())) {
            films.put(film.getId(), film);
        } else {
            throw new ValidationException("There is no movie with this id!");
        }
        return film;
    }

    @Override
    public Collection<Film> findAll() {
        return films.values();
    }

    @Override
    public Film findById(Integer id) {
        if (films.containsKey(id)) {
            return films.get(id);
        } else {
            throw new NotFoundException(String.format("The film with id = %d is not founded.", id));
        }
    }

    @Override
    public void deleteById(Integer id) {
        if (films.containsKey(id)) {
            films.remove(id);
        } else {
            throw new NotFoundException(String.format("The film with id = %d is not founded.", id));
        }
    }
}
