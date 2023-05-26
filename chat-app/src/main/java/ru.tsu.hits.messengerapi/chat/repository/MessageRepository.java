package ru.tsu.hits.messengerapi.chat.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.tsu.hits.messengerapi.chat.entity.ChatEntity;
import ru.tsu.hits.messengerapi.chat.entity.MessageEntity;

import java.util.List;
import java.util.UUID;

/**
 * Репозиторий для работы с сообщениями в чатах.
 */
@Repository
public interface MessageRepository extends JpaRepository<MessageEntity, UUID> {

    /**
     * Получает список MessageEntity по идентификатору чата.
     *
     * @param chatId идентификатор чата
     * @return список MessageEntity
     */
    List<MessageEntity> getAllByChatId(UUID chatId);

    /**
     * Получает первое сообщение MessageEntity из чата, отсортированного по дате отправки в порядке убывания.
     *
     * @param chat чат, из которого нужно получить первое сообщение
     * @return первое сообщение MessageEntity или null, если сообщений в чате нет
     */
    MessageEntity getFirstByChatOrderBySendDateDesc(ChatEntity chat);

    /**
     * Получает список MessageEntity из чата, отсортированный по дате отправки в порядке убывания.
     *
     * @param chat чат, из которого нужно получить список сообщений
     * @return список MessageEntity или пустой список, если сообщений в чате нет
     */
    List<MessageEntity> getAllByChatOrderBySendDateDesc(ChatEntity chat);
}
