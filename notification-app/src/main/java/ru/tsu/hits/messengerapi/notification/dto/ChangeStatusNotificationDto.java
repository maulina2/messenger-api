package ru.tsu.hits.messengerapi.notification.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.tsu.hits.messengerapi.notification.enumeration.NotificationStatus;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;

/**
 * Дто, в которой получается информация для того, чтобы изменить статус прочтения уведомления.
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class ChangeStatusNotificationDto {

    @NotNull(message = "Список уведомлений не должен быть null")
    @NotEmpty(message = "Список уведомлений не должен быть пустым")
    private List<UUID> notificationIds;

    @NotNull(message = "Статус должен быть указан")
    private NotificationStatus notificationStatus;
}
