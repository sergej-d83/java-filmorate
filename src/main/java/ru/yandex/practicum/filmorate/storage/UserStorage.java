package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserStorage {

    List<User> getAllUsers();

    User getUserById(Integer userId);

    List<User> getUserFriends(Integer userId);

    void createUser(User user);

    void updateUser(User user);
}
