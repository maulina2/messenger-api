package ru.tsu.hits.messengerapi.user.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.tsu.hits.messengerapi.common.dto.UserDto;

/**
 * Дто в которой передаются данные о пользователе вместе с токеном.
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class FullUserDto {
    private UserDto userDto;
    private String jwtToken;
}
