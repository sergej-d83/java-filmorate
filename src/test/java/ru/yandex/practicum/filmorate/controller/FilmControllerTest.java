package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class FilmControllerTest {
    FilmController controller;
    Film film;

    @BeforeEach
    public void setUp() {
        controller = new FilmController(new FilmService(new InMemoryFilmStorage()));
        film = new Film(1, "test", "film",
                LocalDate.of(2022, 1, 1), 120);
    }

    @Test
    void shouldCreateNewFilm() {
        assertEquals(film, controller.createFilm(film));
    }

    @Test
    void shouldThrowExceptionReleaseToEarly() {
        film.setReleaseDate(LocalDate.of(1894, 12, 28));
        assertThrows(ValidationException.class, () -> controller.createFilm(film));
    }

    @Test
    void shouldUpdateFilm() {
        Film filmTest = controller.createFilm(film);
        assertEquals(filmTest, film);
        film.setName("updated film");
        controller.updateFilm(film);
        assertEquals(filmTest, film);
    }

    @Test
    void shouldThrowExceptionUpdateIncorrectId() {
        controller.createFilm(film);
        film.setId(10);
        assertThrows(NotFoundException.class, () -> controller.updateFilm(film));
    }

}