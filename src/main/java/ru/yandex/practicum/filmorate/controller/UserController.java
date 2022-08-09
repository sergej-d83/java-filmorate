package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@Slf4j
@RequestMapping("/users")
public class UserController {
    private int userId = 1;
    private final Map<Integer, User> users = new HashMap<>();

    @GetMapping
    public List<User> getAllUsers() {
        log.info("Запрос всех пользователей.");
        return new ArrayList<>(users.values());
    }

    @PostMapping
    public User createUser(@RequestBody User user) {
        if (isValidUser(user)) {
            user.setId(userId++);
            users.put(user.getId(), user);
            log.info("Создан новый пользователь {}", user);
        }
        return user;
    }

    @PutMapping
    public User updateUser(@RequestBody User user) {
        if (isValidUser(user)) {
            if (users.containsKey(user.getId())) {
                users.put(user.getId(), user);
                log.info("Данные пользователя обновлены.");
            } else {
                log.error("Пользователь с таким номером не существует.");
                throw new ValidationException("Пользователь с таким номером не существует.");
            }
        }
        return user;
    }

    private boolean isValidUser(User user) {

        if (user.getEmail().isBlank() || !user.getEmail().contains("@")) {
            log.error("Электронная почта не может быть пустой и должна содержать символ @.");
            throw new ValidationException("Электронная почта не может быть пустой и должна содержать символ @.");
        }
        if (user.getLogin().isBlank() || user.getLogin().contains(" ")) {
            log.error("Логин не может быть пустым или содержать пробелы.");
            throw new ValidationException("Логин не может быть пустым или содержать пробелы.");
        }
        if (user.getBirthday().isAfter(LocalDate.now())) {
            log.error("Дата рождения не может быть в будущем");
            throw new ValidationException("Дата рождения не может быть в будущем");
        }
        if (user.getName().isBlank()) {
            user.setName(user.getLogin());
            log.info("Имя для отображения пустое. Используется логин.");
        }
        return true;
    }
}
