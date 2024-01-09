package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.GenreStorage;

import java.util.Collection;

@Service
@Slf4j
public class GenreService {

    private final GenreStorage genreStorage;

    @Autowired
    public GenreService(GenreStorage genreStorage) {
        this.genreStorage = genreStorage;
    }

    public Collection<Genre> findAll() {
        log.debug("Получен запрос на получение списка всех жанров");
        return genreStorage.findAll();
    }

    public Genre genreById(Integer genreId) {
        log.debug("Получен запрос на получение жанра с id=" + genreId);
        return genreStorage.genreById(genreId);
    }
}