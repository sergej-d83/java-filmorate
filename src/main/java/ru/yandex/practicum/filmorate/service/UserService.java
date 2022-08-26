package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UserService {

    private final UserStorage userStorage;

    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public List<User> getAllUsers() {
        return userStorage.getAllUsers();
    }

    public User getUserById(Integer userId) {
        return userStorage.getUserById(userId);
    }

    public List<User> getUserFriends(Integer userId) {
        return userStorage.getUserFriends(userId);
    }

    public User createUser(User user) {
        return userStorage.createUser(user);
    }

    public User updateUser(User user) {
        return userStorage.updateUser(user);
    }

    public void addFriend(Integer userId, Integer otherUserId) {
        log.info("Добавление в друзья пользователя с ID: {} пользователю с ID: {}", otherUserId, userId);
        User user = userStorage.getUserById(userId);
        User otherUser = userStorage.getUserById(otherUserId);

        user.getFriends().add(otherUser.getId());
        otherUser.getFriends().add(user.getId());
    }

    public void removeFriend(Integer userId, Integer friendId) {
        log.info("Удаление друга с ID: {} у пользователя с ID: {}", friendId, userId);
        User user = userStorage.getUserById(userId);
        User friend = userStorage.getUserById(friendId);

        user.getFriends().remove(friend.getId());
        friend.getFriends().remove(user.getId());
    }

    public List<User> getCommonFriends(Integer userId, Integer otherId) {
        log.info("Запрос общих друзей пользователей с ID: {} и {}", userId, otherId);
        List<Integer> friendsOfUser = new ArrayList<>(userStorage.getUserById(userId).getFriends());
        List<Integer> friendsOfOtherUser = new ArrayList<>(userStorage.getUserById(otherId).getFriends());

        return friendsOfUser.stream()
                .filter(friendsOfOtherUser::contains)
                .map(userStorage::getUserById)
                .collect(Collectors.toList());
    }
}
