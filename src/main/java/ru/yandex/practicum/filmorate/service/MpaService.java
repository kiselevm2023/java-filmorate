package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.MpaStorage;

import java.util.Collection;

@Service
@Slf4j
public class MpaService {
    private final MpaStorage mpaStorage;

    @Autowired
    public MpaService(MpaStorage mpaStorage) {
        this.mpaStorage = mpaStorage;
    }

    public Collection<Mpa> findAll() {
        log.debug("Получен запрос на получение списка всех рейтингов");
        return mpaStorage.findAll();
    }

    public Mpa mpaById(Integer mpaId) {
        log.debug("Получен запрос на получение рейтинга с id=" + mpaId);
        return mpaStorage.mpaById(mpaId);
    }
}
