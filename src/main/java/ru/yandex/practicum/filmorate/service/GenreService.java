package ru.yandex.practicum.filmorate.service;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.film.Genre;
import ru.yandex.practicum.filmorate.storage.dao.GenreDao;

import java.util.Collection;

@Slf4j
@Service
@Data
public class GenreService {
    private final GenreDao genreDao;

    public Collection<Genre> getAllGenres() {
        return genreDao.getAllGenres();
    }

    public Genre getGenreById(Integer genreId) {
        if (!genreDao.isGenrePresent(genreId)) {
            log.info("Жанр с ID: {} не найден", genreId);
            throw new NotFoundException("Жанр с ID: " + genreId + " не найден");
        }
        return genreDao.getGenre(genreId);
    }
}
