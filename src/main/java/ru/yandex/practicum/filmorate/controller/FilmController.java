package ru.yandex.practicum.filmorate.controller;

import lombok.Data;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.film.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import javax.validation.Valid;
import java.util.Collection;

@RestController
@RequestMapping("/films")
@Data
public class FilmController {
    private final FilmService filmService;

    @GetMapping
    public Collection<Film> getAllFilms() {
        return filmService.getAllFilms();
    }

    @GetMapping("/{id}")
    public Film getFilmById(@PathVariable Integer id) {
        return filmService.getFilmById(id);
    }

    @GetMapping("/popular")
    public Collection<Film> getPopularFilms(@RequestParam(name = "count", defaultValue = "10") int count) {
        return filmService.getMostPopularFilms(count);
    }

    @PostMapping
    public Film createFilm(@Valid @RequestBody Film film) {
        return filmService.createFilm(film);
    }

    @PutMapping
    public Film updateFilm(@Valid @RequestBody Film film) {
        return filmService.updateFilm(film);
    }

    @PutMapping("/{filmId}/like/{userId}")
    public void addLike(@PathVariable Integer userId, @PathVariable Integer filmId) {
        filmService.addLike(filmId, userId);
    }

    @DeleteMapping("/{userId}/like/{filmId}")
    public void removeLike(@PathVariable Integer userId, @PathVariable Integer filmId) {
        filmService.removeLike(filmId, userId);
    }
}
