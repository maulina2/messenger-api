package ru.tsu.hits.messengerapi.common.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

/**
 * Дто в которой передаются данные для проверки пользователя на нахождение в черном списке.
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class IsBlockedUserDto {

    private  UUID externalUserId;

    private UUID targetUserId;
}
