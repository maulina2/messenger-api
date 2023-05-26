package ru.tsu.hits.messengerapi.user.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Дто в которой передаются данные пользователя для аутентификаци.
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class SignInDto {

    private String login;
    private String password;
}
