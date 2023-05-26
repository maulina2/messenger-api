package ru.tsu.hits.messengerapi.chat.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.tsu.hits.messengerapi.chat.entity.ChatEntity;

import java.util.UUID;

/**
 * Репозиторий для работы с чатами.
 */
@Repository
public interface ChatRepository extends JpaRepository<ChatEntity, UUID>{

}
