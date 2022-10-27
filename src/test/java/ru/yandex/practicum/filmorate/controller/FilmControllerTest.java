package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.film.Film;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class FilmControllerTest {
    FilmController controller;
    Film film;



    @Test
    void shouldCreateNewFilm() {
        assertEquals(film, controller.createFilm(film));
    }

    @Test
    void shouldThrowExceptionReleaseToEarly() {
        film.setReleaseDate(LocalDate.of(1894, 12, 28));
        ValidationException exception = assertThrows(ValidationException.class, () -> controller.createFilm(film));
        assertEquals("Дата релиза - не раньше 28.12.1895", exception.getMessage());
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
        NotFoundException exception = assertThrows(NotFoundException.class, () -> controller.updateFilm(film));
        assertEquals(String.format("Фильм с таким номером не найден: %s", film.getId()), exception.getMessage());
    }

}