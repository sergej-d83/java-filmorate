package ru.yandex.practicum.filmorate.storage.dao;

import java.util.Collection;

public interface FriendshipDao {
    void addFriend(Integer userId, Integer friendId);

    Collection<Integer> getFriends(Integer userId);

    void deleteFriend(Integer userId, Integer friendId);
}
