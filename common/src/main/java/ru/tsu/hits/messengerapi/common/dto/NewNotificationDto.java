package ru.tsu.hits.messengerapi.common.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.tsu.hits.messengerapi.common.enumeration.NotificationType;

import java.util.UUID;

/**
 * Дто, в которой передается информация о новом уведомлении.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NewNotificationDto {
    private UUID targetUserId;
    private NotificationType type;
    private String message;
}
