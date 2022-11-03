package ru.yandex.practicum.filmorate.storage.dao.impl;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.user.User;
import ru.yandex.practicum.filmorate.storage.dao.UserDao;

import java.sql.Date;
import java.util.Collection;


@Slf4j
@Component("UserDaoImpl")
@Data
public class UserDaoImpl implements UserDao {

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

    @Override
    public Collection<User> getAllUsers() {
        return jdbcTemplate.query("SELECT * FROM users", userMapper);
    }

    @Override
    public User getUserById(Integer userId) {
        String sql = "SELECT * FROM users WHERE user_id = ?";

        User user = jdbcTemplate.queryForObject(sql, userMapper, userId);
        log.info("Возвращаю пользователя: {}", user);

        return user;
    }

    @Override
    public User createUser(User user) {
        String sql = "INSERT INTO users (email, login, user_name, birthday) VALUES(?, ?, ?, ?)";
        jdbcTemplate.update(sql, user.getEmail(), user.getLogin(), user.getName(),
                Date.valueOf(user.getBirthday()));

        String sqlGetUser = "SELECT * FROM users WHERE email = ? AND login = ? AND birthday = ?";
        User createdUser = jdbcTemplate.queryForObject(sqlGetUser, userMapper, user.getEmail(), user.getLogin(),
                user.getBirthday());
        log.info("В базе создан новый пользователь: {}", createdUser);

        return createdUser;
    }

    @Override
    public User updateUser(User user) {
        String sql = "UPDATE users SET email = ?, login = ?, user_name = ?, birthday = ? WHERE user_id = ?";
        jdbcTemplate.update(sql, user.getEmail(), user.getLogin(), user.getName(),
                Date.valueOf(user.getBirthday()), user.getId());

        User updatedUser = getUserById(user.getId());
        log.info("Пользователь обновлён: {}", updatedUser);

        return updatedUser;
    }

    @Override
    public boolean isUserPresent(Integer userId) {
        try {
            getUserById(userId);
            return true;
        } catch (DataAccessException ex) {
            return false;
        }
    }
}
