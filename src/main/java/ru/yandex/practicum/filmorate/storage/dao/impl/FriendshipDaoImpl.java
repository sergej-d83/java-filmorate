package ru.yandex.practicum.filmorate.storage.dao.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.user.User;
import ru.yandex.practicum.filmorate.storage.dao.FriendshipDao;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Slf4j
@Component
public class FriendshipDaoImpl implements FriendshipDao {

    private final JdbcTemplate jdbcTemplate;

    private final RowMapper<User> userMapper = ((rs, rowNum) -> {
        User user = new User();
        user.setId(rs.getInt("user_id"));
        user.setEmail(rs.getString("email"));
        user.setLogin(rs.getString("login"));
        user.setName(rs.getString("user_name"));
        user.setBirthday(rs.getDate("birthday").toLocalDate());

        return user;
    });

    @Autowired
    public FriendshipDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void addFriend(Integer userId, Integer friendId) {
        String sql = "INSERT INTO friendships (user_id, friend_id) VALUES(?, ?)";
        int insert = jdbcTemplate.update(sql, userId, friendId);

        if (insert == 1) {
            log.info("Пользователю ID: {} добавлен друг ID: {}", userId, friendId);
        }
    }

    @Override
    public List<Integer> getFriends(Integer userId) {
        String sql = "SELECT friend_id FROM friendships WHERE user_id = ?";
        List<Integer> friends = new ArrayList<>();
        try {
            friends = jdbcTemplate.queryForList(sql, Integer.class, userId);
        } catch (NotFoundException ex) {
            log.info("Друзья не найдены");
        }
        return friends;
    }

    @Override
    public void deleteFriend(Integer userId, Integer friendId) {
        String sql = "DELETE FROM friendships WHERE user_id = ? AND friend_id = ?";
        int delete = jdbcTemplate.update(sql, userId, friendId);

        if (delete == 1) {
            log.info("Друг ID: {} удалён у пользователя ID: {}", friendId, userId);
        }
    }
}
