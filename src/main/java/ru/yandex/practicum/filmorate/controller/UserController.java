package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
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
    public User createUser(@Valid @RequestBody User user) {

        if (user.getName().isBlank()) {
            user.setName(user.getLogin());
            log.info("Имя для отображения пустое. Используется логин.");
        }

        user.setId(userId++);
        users.put(user.getId(), user);
        log.info("Создан новый пользователь {}", user);
        return user;
    }

    @PutMapping
    public User updateUser(@Valid @RequestBody User user) {

        if (users.containsKey(user.getId())) {
            if (user.getName().isBlank()) {
                user.setName(user.getLogin());
                log.info("Имя для отображения пустое. Используется логин.");
            }
            users.put(user.getId(), user);
            log.info("Данные пользователя обновлены. {}", user);
        } else {
            log.error("Пользователь с таким номером не существует. ID: {}", user.getId());
            throw new ValidationException("Пользователь с таким номером не существует.");
        }
        return user;
    }
}
