package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class FilmControllerTest {
    FilmController controller;

    @BeforeEach
    public void userController() {
        controller = new FilmController();
    }

    @Test
    void shouldThrowExceptionNameIsBlank() {
        Film film = new Film(1, " ", "film",
                LocalDate.of(1983, 10, 26), 120);
        assertThrows(ValidationException.class, () -> controller.createFilm(film));
    }

    @Test
    void shouldThrowExceptionDescriptionToLong() {
        Film film = new Film(1, "test", "The Godfather \"Don\" Vito Corleone " +
                "is the head of the Corleone mafia family in New York." +
                " He is at the event of his daughter's wedding. " +
                "Michael, Vito's youngest son and a decorated WW II Marine is also present",
                LocalDate.of(1983, 10, 26), 120);
        assertThrows(ValidationException.class, () -> controller.createFilm(film));
    }

    @Test
    void shouldThrowExceptionReleaseToEarly() {
        Film film = new Film(1, "test", "film",
                LocalDate.of(1894, 12, 28), 120);
        assertThrows(ValidationException.class, () -> controller.createFilm(film));
    }

    @Test
    void shouldThrowExceptionDurationToShort() {
        Film film = new Film(1, "test", "film",
                LocalDate.of(1896, 12, 28), 0);
        assertThrows(ValidationException.class, () -> controller.createFilm(film));
    }

    @Test
    void shouldThrowExceptionUpdateIncorrectId() {
        Film film = new Film(1, "test", "film",
                LocalDate.of(1983, 10, 26), 120);
        assertThrows(ValidationException.class, () -> controller.updateFilm(film));
    }

}