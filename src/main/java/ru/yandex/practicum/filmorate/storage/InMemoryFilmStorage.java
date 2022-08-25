package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.AlreadyExistsException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class InMemoryFilmStorage implements FilmStorage {

    private Integer id = 1;
    private final Map<Integer, Film> films = new HashMap<>();
    private final LocalDate EARLIEST_DATE = LocalDate.of(1895, 12, 28);

    public List<Film> getAllFilms() {
        log.info("Запрос всех фильмов.");
        return new ArrayList<>(films.values());
    }

    @Override
    public Film getFilmById(Integer filmId) {
        log.info("Запрос фильма с ID: {}", filmId);
        if (!films.containsKey(filmId)) {
            log.error("Фильм с таким номером не найден: {}", filmId);
            throw new NotFoundException(String.format("Фильм с таким номером не найден: %s", filmId));
        }
        return films.get(filmId);
    }

    @Override
    public void createFilm(Film film) {
        validateReleaseDate(film);

        if (films.containsValue(film)) {
            log.error("Такой фильм уже существует");
            throw new AlreadyExistsException("Такой фильм уже существует");
        }

        film.setId(id++);
        films.put(film.getId(), film);
        log.info("Добавлен новый фильм: {}", film);
    }

    @Override
    public void updateFilm(Film film) {

        if (films.containsKey(film.getId())) {
            validateReleaseDate(film);
            films.put(film.getId(), film);
            log.info("Фильм обновлен: {}", film);
        } else {
            log.error("Фильм с таким номером не найден. ID: {}", film.getId());
            throw new NotFoundException(String.format("Фильм с таким номером не найден: %s", film.getId()));
        }
    }


    private void validateReleaseDate(Film film) {
        if (film.getReleaseDate().isBefore(EARLIEST_DATE)) {
            log.error("Дата релиза - не раньше 28.12.1895");
            throw new ValidationException("Дата релиза - не раньше 28.12.1895");
        }
    }
}
