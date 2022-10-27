package ru.yandex.practicum.filmorate.storage.dao;

import java.util.List;

public interface FriendshipDao {
    void addFriend(Integer userId, Integer friendId);
    List<Integer> getFriends(Integer userId);
    void deleteFriend(Integer userId, Integer friendId);
}
