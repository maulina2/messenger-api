package ru.tsu.hits.messengerapi.notification.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.tsu.hits.messengerapi.common.enumeration.NotificationType;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Дто, в которой передается информация об уведомлении.
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class NotificationDto {
    private UUID id;

    private NotificationType notificationType;

    private String text;

    @JsonFormat(pattern = "yyyy-MM-dd hh:mm")
    private LocalDateTime receivingDate;

    private boolean isRead;
}
