package ru.tsu.hits.messengerapi.notification.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.tsu.hits.messengerapi.common.enumeration.NotificationType;

import java.util.List;

/**
 * Дто, в которой передаются фильтры для получения списка уведомлений.
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class NotificationFiltersDto {

    private PeriodDto periodFilter;

    private String textFilter;

    private List<NotificationType> notificationTypes;
}
