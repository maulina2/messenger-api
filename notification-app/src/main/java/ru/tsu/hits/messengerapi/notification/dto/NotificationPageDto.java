package ru.tsu.hits.messengerapi.notification.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

/**
 * Дто, в которой возвращается список уведомлений.
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class NotificationPageDto {

    private Page<NotificationDto> notificationDtoList;

    private NotificationFiltersDto notificationFiltersDto;
}
