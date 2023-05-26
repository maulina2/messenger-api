package ru.tsu.hits.messengerapi.chat.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.tsu.hits.messengerapi.chat.entity.ChatEntity;
import ru.tsu.hits.messengerapi.chat.entity.ChatUserEntity;
import ru.tsu.hits.messengerapi.chat.repository.ChatRepository;
import ru.tsu.hits.messengerapi.chat.repository.ChatUserRepository;
import ru.tsu.hits.messengerapi.common.exception.ForbiddenException;
import ru.tsu.hits.messengerapi.common.exception.NotFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Сервис, в котором лежат общие методы сервиса чатов и сообщений.
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ChatMessageService {

    private final ChatRepository chatRepository;
    private final ChatUserRepository chatUserRepository;


    /**
     * Проверяет, имеет ли пользователь доступ к чату с заданным идентификатором.
     *
     * @param chatId       идентификатор чата
     * @param targetUserId идентификатор пользователя, который запрашивает информацию о чате
     * @throws ForbiddenException если пользователь не имеет доступа к запрашиваемому чату
     */
    public void checkTargetUser(UUID chatId, UUID targetUserId) {

        List<ChatUserEntity> chatUserEntities = chatUserRepository.getByChatId(chatId);
        for (ChatUserEntity chatUserEntity : chatUserEntities) {
            if (chatUserEntity.getUserId().equals(targetUserId)) {
                return;
            }
        }
        throw new ForbiddenException("У вас нет доступа к этому чату.");
    }

    /**
     * Ищет чат с заданным идентификатором в репозитории и возвращает его объект, если он найден.
     *
     * @param chatId идентификатор чата
     * @return объект ChatEntity, соответствующий запрашиваемому чату
     * @throws NotFoundException если чат не найден
     */
    public ChatEntity findChat(UUID chatId) {
        return chatRepository.findById(chatId)
                .orElseThrow(() -> {
                    throw new NotFoundException("Чат не найден.");
                });
    }


    /**
     * Возвращает список id чатов, в которых состоит целевой пользователь.
     *
     * @param targetUserId UUID, представляющий идентификатор целевого пользователя
     * @return список UUID, представляющих идентификаторы чатов
     */
    public List<ChatEntity> getTargetUserChats(UUID targetUserId) {

        List<ChatUserEntity> chatUserEntities = chatUserRepository.getByUserId(targetUserId);
        List<UUID> chatIds = new ArrayList<>();
        for (ChatUserEntity chatUserEntity : chatUserEntities) {
            chatIds.add(chatUserEntity.getChatId());
        }

        List<ChatEntity> chats = new ArrayList<>();

        for (UUID chatId : chatIds) {
            Optional<ChatEntity> chat = chatRepository.findById(chatId);
            chat.ifPresent(chats::add);
        }
        return chats;
    }
}
