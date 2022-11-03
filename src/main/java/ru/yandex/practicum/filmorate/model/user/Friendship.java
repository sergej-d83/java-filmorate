package ru.yandex.practicum.filmorate.model.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Friendship {
    @NotNull
    private Integer userId;
    @NotNull
    private Integer friendId;
    @NotNull
    private boolean isConfirmed;
}
