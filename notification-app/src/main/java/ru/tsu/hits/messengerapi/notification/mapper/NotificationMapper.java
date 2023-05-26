package ru.tsu.hits.messengerapi.notification.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.tsu.hits.messengerapi.common.dto.NewNotificationDto;
import ru.tsu.hits.messengerapi.notification.dto.NotificationDto;
import ru.tsu.hits.messengerapi.notification.entity.NotificationEntity;

import java.time.LocalDateTime;

/**
 * Класс для маппинга различных dto и {@link ru.tsu.hits.messengerapi.notification.entity.NotificationEntity}.
 */
@Component
@RequiredArgsConstructor
public class NotificationMapper {

    /**
     * Преобразует объект NotificationEntity в объект NotificationDto.
     *
     * @param entity объект, который нужно преобразовать
     * @return объект NotificationDto
     */
    public NotificationDto entityToNotificationDto(NotificationEntity entity) {
        return new NotificationDto(
                entity.getId(),
                entity.getNotificationType(),
                entity.getText(),
                entity.getReceivingDate(),
                entity.isRead()
        );
    }

    /**
     * Преобразует объект NewNotificationDto в объект NotificationEntity.
     *
     * @param dto объект, который нужно преобразовать
     * @return объект NotificationEntity
     */
    public NotificationEntity dtoToNotificationEntity(NewNotificationDto dto) {
        return NotificationEntity
                .builder()
                .notificationType(dto.getType())
                .text(dto.getMessage())
                .isRead(false)
                .receivingDate(LocalDateTime.now())
                .userId(dto.getTargetUserId())
                .build();
    }

}
