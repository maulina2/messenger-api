package ru.tsu.hits.messengerapi.chat.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.tsu.hits.messengerapi.chat.dto.ChatDto;
import ru.tsu.hits.messengerapi.chat.dto.CreateChatDto;
import ru.tsu.hits.messengerapi.chat.dto.ExtendedChatDto;
import ru.tsu.hits.messengerapi.chat.entity.ChatEntity;
import ru.tsu.hits.messengerapi.chat.entity.ChatUserEntity;
import ru.tsu.hits.messengerapi.chat.entity.MessageEntity;
import ru.tsu.hits.messengerapi.chat.enumeration.ChatType;
import ru.tsu.hits.messengerapi.common.service.ConvertDateService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Класс для маппинга различных dto и {@link ChatEntity}.
 */
@Component
@RequiredArgsConstructor
public class ChatMapper {

    private final ConvertDateService convertDateService;

    /**
     * Создает и возвращает объект ChatEntity на основе переданных данных.
     *
     * @param createChatDto объект, содержащий информацию о создаваемом чате
     * @param targetUserId идентификатор пользователя-администратора создаваемого чата
     * @return объект ChatEntity, содержащий информацию о созданном чате
     */
    public ChatEntity createChat(CreateChatDto createChatDto, UUID targetUserId) {
        return ChatEntity
                .builder()
                .name(createChatDto.getName())
                .admin(targetUserId)
                .avatarId(createChatDto.getAvatarId())
                .chatCreationDate(convertDateService.convertToDate(LocalDate.now()))
                .chatType(ChatType.CHAT)
                .build();
    }

    /**
     * Создает и возвращает объект ChatEntity для диалога.
     *
     * @param name имя диалога
     * @return объект ChatEntity, содержащий информацию о диалоге
     */
    public ChatEntity createDialog(String name) {
        return ChatEntity
                .builder()
                .name(name)
                .chatType(ChatType.DIALOG)
                .build();
    }

    /**
     * Создает и возвращает объект ChatUserEntity на основе переданных данных.
     *
     * @param chatId идентификатор чата
     * @param userId идентификатор пользователя
     * @return объект ChatUserEntity, содержащий информацию о привязке пользователя к чату
     */
    public ChatUserEntity createChatUserEntity(UUID chatId, UUID userId){
        return ChatUserEntity
                .builder()
                .chatId(chatId)
                .userId(userId)
                .build();
    }

    /**
     * Преобразует объект ChatEntity в объект ChatDto.
     *
     * @param chatEntity объект, содержащий информацию о чате
     * @return объект ChatDto, содержащий информацию о чате
     */
    public ChatDto chatToChatDto(ChatEntity chatEntity){
        return new ChatDto(
                chatEntity.getId(),
                chatEntity.getChatType(),
                chatEntity.getChatCreationDate(),
                chatEntity.getName(),
                chatEntity.getAdmin(),
                chatEntity.getAvatarId()
        );
    }

    /**
     * Метод для преобразования объекта ChatEntity и объекта MessageEntity в объект ExtendedChatDto.
     *
     * @param chatEntity объект, содержащий информацию о чате
     * @param message объект, содержащий информацию о сообщении
     * @return объект ExtendedChatDto, содержащий информацию о чате и последнем сообщении в нём
     */
    public ExtendedChatDto chatToChatDateDto(ChatEntity chatEntity, MessageEntity message){
        return new ExtendedChatDto(
                chatEntity.getId(),
                chatEntity.getName(),
                chatEntity.getChatType(),
                message.getSendDate(),
                message.getText(),
                message.getAttachmentEntity().size() != 0,
                message.getAuthorId()
        );
    }
    /**
     * Метод для преобразования объекта ChatEntity в объект ExtendedChatDto.
     *
     * @param chatEntity объект, содержащий информацию о чате
     * @return объект ExtendedChatDto, содержащий информацию о чате и последнем сообщении в нём
     */
    public ExtendedChatDto chatToChatDateDto(ChatEntity chatEntity){
        return new ExtendedChatDto(
                chatEntity.getId(),
                chatEntity.getName(),
                chatEntity.getChatType(),
                LocalDateTime.of(1914, 12, 31, 0, 0),
                null,
                false,
                null
        );
    }
}
