package ru.yandex.practicum.filmorate.storage.dao.impl;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.storage.dao.FriendshipDao;

import java.util.Collection;

@Slf4j
@Component
@Data
public class FriendshipDaoImpl implements FriendshipDao {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public void addFriend(Integer userId, Integer friendId) {
        String sql = "INSERT INTO friendships (user_id, friend_id) VALUES(?, ?)";
        int insert = jdbcTemplate.update(sql, userId, friendId);

        if (insert == 1) {
            log.info("Пользователю с ID {} добавлен друг с ID {}", userId, friendId);
        }
    }

    @Override
    public Collection<Integer> getFriends(Integer userId) {
        String sql = "SELECT friend_id FROM friendships WHERE user_id = ?";

        return jdbcTemplate.queryForList(sql, Integer.class, userId);
    }

    @Override
    public void deleteFriend(Integer userId, Integer friendId) {
        String sql = "DELETE FROM friendships WHERE user_id = ? AND friend_id = ?";
        int delete = jdbcTemplate.update(sql, userId, friendId);

        if (delete == 1) {
            log.info("Друг с ID {} удалён у пользователя с ID {}", friendId, userId);
        }
    }
}
