package ru.tsu.hits.messengerapi.commonsecurity.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.util.UUID;

/**
 * Данные Principal в Authentication - информация о текущем пользователе
 */
@Getter
@AllArgsConstructor
@ToString
public class JwtUserData {

    private final UUID id;

    private final String login;

    private final String fullName;

    private final String email;

}
