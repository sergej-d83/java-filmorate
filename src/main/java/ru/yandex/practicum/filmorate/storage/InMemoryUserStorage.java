package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.AlreadyExistsException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;
import java.util.stream.Collectors;

@Component
@Slf4j
public class InMemoryUserStorage implements UserStorage {

    private Integer id = 1;
    private final Map<Integer, User> users = new HashMap<>();

    @Override
    public User getUserById(Integer userId) {
        log.info("Запрос пользователя с ID: {}", userId);

        if (!users.containsKey(userId)) {
            log.error("Пользователь с таким номером не найден: {}", userId);
            throw new NotFoundException(
                    String.format("Пользователь с таким номером не найден: %s", userId)
            );
        }
        return users.get(userId);
    }

    @Override
    public List<User> getUserFriends(Integer userId) {
        log.info("Запрос друзей пользователя с ID: {}", userId);

        if (!users.containsKey(userId)) {
            log.error("Пользователь с таким номером не найден: {}", userId);
            throw new NotFoundException(
                    String.format("Пользователь с таким номером не найден: %s", userId)
            );
        }
        return users.get(userId).getFriends().stream()
                .map(this::getUserById)
                .collect(Collectors.toList());
    }

    @Override
    public List<User> getAllUsers() {
        log.info("Запрос всех пользователей.");
        return new ArrayList<>(users.values());
    }

    @Override
    public void createUser(User user) {

        if (users.containsValue(user)) {
            log.error("Такой пользователь уже существует");
            throw new AlreadyExistsException("Такой пользователь уже существует");
        }
        if (user.getName().isBlank()) {
            user.setName(user.getLogin());
            log.info("Имя для отображения пустое. Используется логин.");
        }

        user.setId(id++);
        users.put(user.getId(), user);
        log.info("Создан новый пользователь {}", user);
    }

    @Override
    public void updateUser(User user) {

        if (users.containsKey(user.getId())) {
            if (user.getName().isBlank()) {
                user.setName(user.getLogin());
                log.info("Имя для отображения пустое. Используется логин.");
            }
            users.put(user.getId(), user);
            log.info("Данные пользователя обновлены. {}", user);
        } else {
            log.error("Пользователь с таким номером не найден. ID: {}", user.getId());
            throw new NotFoundException(
                    String.format("Пользователь с таким номером не найден: %s", user.getId()));
        }
    }
}
