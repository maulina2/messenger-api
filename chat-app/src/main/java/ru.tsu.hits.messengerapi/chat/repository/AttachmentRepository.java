package ru.tsu.hits.messengerapi.chat.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.tsu.hits.messengerapi.chat.entity.AttachmentEntity;
import ru.tsu.hits.messengerapi.chat.entity.MessageEntity;

import java.util.List;
import java.util.UUID;

/**
 * Репозиторий для работы с вложениями сообщений.
 */
@Repository
public interface AttachmentRepository extends JpaRepository<AttachmentEntity, UUID> {

    /**
     * Метод, который ищет список вложений по сообщению
     * @param message сообщение, в котором надо найти вложения
     * @return список вложений
     */
    List<AttachmentEntity> findByMessage(MessageEntity message);
}
