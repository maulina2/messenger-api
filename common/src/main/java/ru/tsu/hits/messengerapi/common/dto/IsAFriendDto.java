package ru.tsu.hits.messengerapi.common.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

/**
 * Дто в которой передаются данные для проверки пользователя на нахождение в друзьях.
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class IsAFriendDto {

    private UUID externalUserId;

    private UUID targetUserId;
}
