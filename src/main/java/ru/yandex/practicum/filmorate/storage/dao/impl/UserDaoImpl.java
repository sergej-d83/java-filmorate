package ru.yandex.practicum.filmorate.storage.dao.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    public UserDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

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
        int insert = jdbcTemplate.update(sql, user.getEmail(), user.getLogin(), user.getName(),
                                                          Date.valueOf(user.getBirthday()));
        if (insert == 1) {
            log.info("В базе создан новый пользователь: {}", user.getLogin());
        } else {
            log.info("Пользователь не может быть создан в базе. {}", user);
        }

        String sqlGetUser = "SELECT * FROM users WHERE email = ? AND login = ? AND birthday = ?";

        return jdbcTemplate.queryForObject(sqlGetUser, userMapper, user.getEmail(), user.getLogin(),
                                                                           user.getBirthday()
        );
    }

    @Override
    public User updateUser(User user) {
        String sql = "UPDATE users SET email = ?, login = ?, user_name = ?, birthday = ? WHERE user_id = ?";
        int update = jdbcTemplate.update(sql, user.getEmail(), user.getLogin(), user.getName(),
                                              Date.valueOf(user.getBirthday()), user.getId());
        if (update == 1) {
            log.info("Пользователь обновлён: {}", user.getLogin());
        }
        return user;
    }

    @Override
    public void deleteUser(Integer userId) {
        jdbcTemplate.update("DELETE FROM users WHERE user_id = ?", userId);
        log.info("Пользователь с ID: {} удалён", userId);
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
