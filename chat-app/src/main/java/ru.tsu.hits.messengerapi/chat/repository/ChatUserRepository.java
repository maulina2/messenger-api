package ru.tsu.hits.messengerapi.chat.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.tsu.hits.messengerapi.chat.entity.ChatUserEntity;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Репозиторий для работы с сущностью связи чата и пользователя.
 */
public interface ChatUserRepository extends JpaRepository<ChatUserEntity, UUID> {

   /**
    * Получает список ChatUserEntity по идентификатору чата.
    *
    * @param chatId идентификатор чата
    * @return список ChatUserEntity
    */
   List<ChatUserEntity> getByChatId(UUID chatId);

   /**
    * Получает список ChatUserEntity по идентификатору пользователя.
    *
    * @param userId идентификатор пользователя
    * @return список ChatUserEntity
    */
   List<ChatUserEntity> getByUserId(UUID userId);

   /**
    * Удаляет ChatUserEntity по идентификатору чата.
    *
    * @param chatId идентификатор чата
    */
   void deleteChatUserEntitiesByChatId(UUID chatId);

   /**
    * Находит список ChatUserEntity по идентификатору пользователя.
    *
    * @param userId идентификатор пользователя
    * @return Optional со списком ChatUserEntity, если он найден, иначе пустой Optional
    */
   Optional<List<ChatUserEntity>> findByUserId(UUID userId);
}
