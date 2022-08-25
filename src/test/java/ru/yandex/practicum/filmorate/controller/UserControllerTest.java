package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class UserControllerTest {

    UserController controller;
    User user;

    @BeforeEach
    public void setUp() {
        controller = new UserController(new UserService(new InMemoryUserStorage()));
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
        assertThrows(NotFoundException.class, () -> controller.updateUser(user));
    }

}