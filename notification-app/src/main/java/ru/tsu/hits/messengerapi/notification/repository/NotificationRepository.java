package ru.tsu.hits.messengerapi.notification.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import ru.tsu.hits.messengerapi.notification.entity.NotificationEntity;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Репозиторий для работы с уведомлениями.
 */
@Repository
public interface NotificationRepository extends JpaRepository<NotificationEntity, UUID>, JpaSpecificationExecutor<NotificationEntity> {

    /**
     * Метод, считающий количество уведомлений, которые есть у текущего пользователя.
     * @param userId id текущего пользователя.
     * @return число уведомлений
     */
    int countAllByUserIdAndReadDate(UUID userId, LocalDateTime dateTime);

}
