package ru.yandex.practicum.filmorate.storage.dao;

import ru.yandex.practicum.filmorate.model.user.User;

import java.util.Collection;


public interface UserDao {
    Collection<User> getAllUsers();

    User getUserById(Integer userId);

    User createUser(User user);

    User updateUser(User user);

    void deleteUser(Integer userId);

    boolean isUserPresent(Integer userId);
}
