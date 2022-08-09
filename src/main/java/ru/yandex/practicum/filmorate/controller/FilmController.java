package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@Slf4j
@RequestMapping ("/films")
public class FilmController {
    private int filmId = 1;
    private final Map<Integer, Film> films = new HashMap<>();
    private final int MAX_DESCRIPTION = 200;
    private final LocalDate EARLIEST_DATE = LocalDate.of(1895,12,28);

    @GetMapping
    public List<Film> getAllFilms() {
        log.info("Запрос всех фильмов.");
        return new ArrayList<>(films.values());
    }

    @PostMapping
    public Film createFilm(@RequestBody Film film) {
        if (isValidFilm(film)) {
            film.setId(filmId++);
            films.put(film.getId(), film);
            log.info("Добавлен новый фильм: {}", film);
        }
        return film;
    }

    @PutMapping
    public Film updateFilm(@RequestBody Film film) {
        if (isValidFilm(film)) {
            if (films.containsKey(film.getId())) {
                films.put(film.getId(), film);
                log.info("Фильм обновлен.");
            } else {
                log.error("Фильм с таким номером не существует");
                throw new ValidationException("Фильм с таким номером не существует");
            }
        }
        return film;
    }

    private boolean isValidFilm(Film film) {
        if (film.getName().isBlank()) {
            log.error("Название фильма не может быть пустым.");
            throw new ValidationException("Название фильма не может быть пустым.");
        } else if (film.getDescription().length() > MAX_DESCRIPTION) {
            log.error("Максимальная длина описания — " + MAX_DESCRIPTION + " символов.");
            throw new ValidationException("Максимальная длина описания — " + MAX_DESCRIPTION + " символов.");
        } else if (film.getReleaseDate().isBefore(EARLIEST_DATE)) {
            log.error("Дата релиза - не раньше 28.12.1895");
            throw new ValidationException("Дата релиза - не раньше 28.12.1895");
        } else if (film.getDuration() < 1) {
            log.error("Продолжительность фильма должна быть положительной.");
            throw new ValidationException("Продолжительность фильма должна быть положительной.");
        } else {
            return true;
        }
    }
}
