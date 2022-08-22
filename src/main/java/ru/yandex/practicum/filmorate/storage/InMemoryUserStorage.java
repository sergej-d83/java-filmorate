package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class InMemoryUserStorage implements UserStorage {

    private int userId = 1;
    private final Map<Integer, User> users = new HashMap<>();

    public List<User> getAllUsers() {
        return new ArrayList<>(users.values());
    }

    public User createUser(User user) {

        if (user.getName().isBlank()) {
            user.setName(user.getLogin());
        }

        user.setId(userId++);
        users.put(user.getId(), user);
        return user;
    }

    public User updateUser(User user) {

        if (users.containsKey(user.getId())) {
            if (user.getName().isBlank()) {
                user.setName(user.getLogin());
            }
            users.put(user.getId(), user);
        } else {
            throw new ValidationException("Пользователь с таким номером не существует.");
        }
        return user;
    }
}
