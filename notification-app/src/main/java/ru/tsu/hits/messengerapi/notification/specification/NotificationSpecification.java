package ru.tsu.hits.messengerapi.notification.specification;

import lombok.experimental.UtilityClass;
import org.springframework.data.jpa.domain.Specification;
import ru.tsu.hits.messengerapi.common.enumeration.NotificationType;
import ru.tsu.hits.messengerapi.notification.entity.NotificationEntity;
import ru.tsu.hits.messengerapi.notification.entity.NotificationEntity_;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@UtilityClass
public class NotificationSpecification {

    /**
     * Возвращает спецификацию для поиска уведомлений по идентификатору пользователя.
     *
     * @param id идентификатор пользователя
     * @return спецификация для поиска уведомлений по идентификатору пользователя
     */
    public static Specification<NotificationEntity> userId(UUID id) {
        return (notification, query, builder) -> builder.equal(notification.get(NotificationEntity_.USER_ID), id);
    }

    /**
     * Возвращает спецификацию для поиска уведомлений, содержащих указанную подстроку в тексте сообщения.
     *
     * @param substring подстрока для поиска в тексте сообщения
     * @return спецификация для поиска уведомлений, содержащих указанную подстроку в тексте сообщения
     */
    public static Specification<NotificationEntity> messageContainsSubstring(String substring) {
        return ((notification, query, builder) ->
                builder.like(
                        builder.upper(notification.get(NotificationEntity_.TEXT)),
                        "%" + substring.toUpperCase() + "%")
        );
    }

    /**
     * Возвращает спецификацию для поиска уведомлений, полученных до указанной даты и времени.
     *
     * @param dateTime дата и время, до которых нужно искать уведомления
     * @return спецификация для поиска уведомлений, полученных до указанной даты и времени
     */
    public static Specification<NotificationEntity> receivedDateTimeBefore(LocalDateTime dateTime) {
        return (notification, query, builder) ->
                builder.lessThanOrEqualTo(notification.get(NotificationEntity_.RECEIVING_DATE), dateTime);
    }

    /**
     * Возвращает спецификацию для поиска уведомлений, полученных после указанной даты и времени.
     *
     * @param dateTime дата и время, после которых нужно искать уведомления
     * @return спецификация для поиска уведомлений, полученных после указанной даты и времени
     */
    public static Specification<NotificationEntity> receivedDateTimeAfter(LocalDateTime dateTime) {
        return (notification, query, builder) ->
                builder.greaterThanOrEqualTo(notification.get(NotificationEntity_.RECEIVING_DATE), dateTime);
    }

    /**
     * Возвращает спецификацию для поиска уведомлений, тип которых входит в указанный список типов.
     *
     * @param types список типов уведомлений
     * @return спецификация для поиска уведомлений, тип которых входит в указанный список типов
     */
    public static Specification<NotificationEntity> typeIn(List<NotificationType> types) {
        return ((notification, query, builder) -> builder.in(notification.get(NotificationEntity_.NOTIFICATION_TYPE)).value(types));
    }

}
