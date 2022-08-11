package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class UserControllerTest {

    UserController controller;
    User user;

    @BeforeEach
    public void setUp() {
        controller = new UserController();
        user = new User(1, "test@test.com", "user", "name",
                LocalDate.of(1983, 10, 26));
    }

    @Test
    void shouldUpdateUser() {
        User testUser = controller.createUser(user);
        assertEquals(testUser, user);
        user.setName("updated user");
        testUser = controller.updateUser(user);
        assertEquals(testUser, user);
    }

    @Test
    void shouldThrowExceptionUpdateIncorrectId() {
        controller.createUser(user);
        user.setId(10);
        assertThrows(ValidationException.class, () -> controller.updateUser(user));
    }

}