package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.AlreadyExistsException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.user.User;
import ru.yandex.practicum.filmorate.storage.dao.FriendshipDao;
import ru.yandex.practicum.filmorate.storage.dao.UserDao;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service("UserService")
@Slf4j
public class UserService {

    private final UserDao userDao;
    private final FriendshipDao friendshipDao;

    @Autowired
    public UserService(UserDao userDao, FriendshipDao friendshipDao) {
        this.userDao = userDao;
        this.friendshipDao = friendshipDao;
    }

    public Collection<User> getAllUsers() {
        return userDao.getAllUsers();
    }

    public User getUserById(Integer userId) {
        if (!userDao.isUserPresent(userId)) {
            log.info("Пользователь с ID: {} не найден", userId);
            throw new NotFoundException("Пользователь с ID: " + userId + "не найден");
        }
        return userDao.getUserById(userId);
    }

    public User createUser(User user) {
        if (user.getName().isBlank() || user.getName() == null) {
            user.setName(user.getLogin());
        }

        return userDao.createUser(user);
    }

    public User updateUser(User user) {
        if (!userDao.isUserPresent(user.getId())) {
            log.info("Пользователь с ID: {} не найден", user.getId());
            throw new NotFoundException("Пользователь с ID: " + user.getId() + "не найден");
        }
        return userDao.updateUser(user);
    }

    public void deleteUser(Integer userId) {
        if (!userDao.isUserPresent(userId)) {
            log.info("Пользователь с ID: {} не найден",userId);
            throw new NotFoundException("Пользователь с ID: " + userId + "не найден");
        }
        userDao.deleteUser(userId);
    }

    public void addFriend(Integer userId, Integer friendId) {
        checkUserData(userId);
        checkUserData(friendId);

        log.info("Добавление в друзья пользователя с ID: {} пользователю с ID: {}", friendId, userId);
        friendshipDao.addFriend(userId, friendId);
    }

    public void removeFriend(Integer userId, Integer friendId) {
        log.info("Удаление друга с ID: {} у пользователя с ID: {}", friendId, userId);
        friendshipDao.deleteFriend(userId, friendId);
    }

    public Collection<User> getFriends(Integer userId) {
        if (!userDao.isUserPresent(userId)) {
            log.info("Пользователь с ID: {} не найден", userId);
            throw new NotFoundException("Пользователь с ID: " + userId + " не найден");
        }

        List<User> friends = new ArrayList<>();

        List<Integer> idsOfFriends = (List<Integer>) friendshipDao.getFriends(userId);
        if (idsOfFriends.isEmpty()) {
            return friends;
        } else {
            for (Integer id : idsOfFriends) {
                User friend = userDao.getUserById(id);
                friends.add(friend);
            }
        }
        return friends;
    }

    public Collection<User> getCommonFriends(Integer userId, Integer friendId) {
        log.info("Запрос общих друзей пользователей с ID: {} и {}", userId, friendId);
        List<User> friendsOfUser = (List<User>) getFriends(userId);
        List<User> friendsOfOtherUser = (List<User>) getFriends(friendId);

        return friendsOfUser.stream()
                .filter(friendsOfOtherUser::contains)
                .collect(Collectors.toList());
    }

    private void checkUserData(Integer userId) {
        if (userId > 0) {
            if (!userDao.isUserPresent(userId)) {
                log.info("Пользователь с таким ID: {} уже существует", userId);
                throw new AlreadyExistsException("Пользователь с таким ID: " + userId + " уже существует");
            }
        } else {
            log.info("Пользователей с таким идентификатором не существует: {}", userId);
            throw new NotFoundException("Пользователей с таким идентификатором не существует: " + userId);
        }
    }
}
