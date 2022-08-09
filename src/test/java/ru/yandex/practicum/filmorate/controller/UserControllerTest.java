package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class UserControllerTest {

    UserController controller;

    @BeforeEach
    public void userController() {
        controller = new UserController();
    }

    @Test
    void shouldThrowExceptionUpdateIncorrectId() {
        User user = new User(1, "test@test.com", "user", "name",
                LocalDate.of(1983, 10, 26));
        assertThrows(ValidationException.class, () -> controller.updateUser(user));
    }

}