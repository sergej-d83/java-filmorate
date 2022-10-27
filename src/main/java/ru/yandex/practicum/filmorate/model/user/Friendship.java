package ru.yandex.practicum.filmorate.model.user;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class Friendship {
    @NotNull
    private Integer userId;
    @NotNull
    private Integer friendId;
    @NotNull
    private boolean isConfirmed;
}
