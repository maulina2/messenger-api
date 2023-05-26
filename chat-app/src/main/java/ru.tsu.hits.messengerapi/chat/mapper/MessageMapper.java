package ru.tsu.hits.messengerapi.chat.mapper;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.tsu.hits.messengerapi.chat.dto.*;
import ru.tsu.hits.messengerapi.chat.entity.AttachmentEntity;
import ru.tsu.hits.messengerapi.chat.entity.ChatEntity;
import ru.tsu.hits.messengerapi.chat.entity.MessageEntity;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;


/**
 * Класс для маппинга различных dto и {@link MessageEntity}.
 */
@Component
@RequiredArgsConstructor
public class MessageMapper {

    /**
     * Создает и возвращает объект MessageEntity на основе переданных данных.
     *
     * @param dto объект, содержащий информацию о сообщении
     * @param chat объект, содержащий информацию о чате, в который отправляется сообщение
     * @return объект MessageEntity, содержащий информацию о сообщении
     */
    public MessageEntity createMessage(SendMessageInChatDto dto, ChatEntity chat, UUID targetUserId) {
        return MessageEntity
                .builder()
                .chat(chat)
                .text(dto.getText())
                .sendDate(LocalDateTime.now())
                .authorId(targetUserId)
                .build();
    }

    /**
     * Создает и возвращает объект MessageEntity на основе переданных данных.
     *
     * @param dto объект, содержащий информацию о сообщении
     * @param chat объект, содержащий информацию о диалоге, в который отправляется сообщение
     * @return объект MessageEntity, содержащий информацию о сообщении
     */
    public MessageEntity createMessage(SendMessageInDialogDto dto, ChatEntity chat, UUID targetUserId) {
        return MessageEntity
                .builder()
                .chat(chat)
                .text(dto.getText())
                .sendDate(LocalDateTime.now())
                .authorId(targetUserId)
                .build();
    }

    /**
     * Преобразует объект MessageEntity в объект MessageDto с указанием идентификатора пользователя-получателя.
     *
     * @param message объект, содержащий информацию о сообщении
     * @param targetUserId идентификатор пользователя-получателя
     * @return объект MessageDto, содержащий информацию о сообщении
     */
    public MessageDto messageToMessageDto(MessageEntity message, UUID targetUserId, List<AttachmentDto> attachments){
        return new MessageDto(
                message.getId(),
                message.getSendDate(),
                message.getText(),
                targetUserId,
                attachments
        );
    }

    /**
     * Метод для преобразования объектов ChatEntity и MessageEntity в объект FoundMessageDto.
     *
     * @param chat объект, содержащий информацию о чате
     * @param message объект, содержащий информацию о сообщении
     * @return объект FoundMessageDto, содержащий информацию о найденном сообщении
     */
    public FoundMessageDto messageToFoundMessageDto(ChatEntity chat, MessageEntity message, List<AttachmentEntity> attachments){
        return new FoundMessageDto(
                chat.getId(),
                chat.getName(),
                message.getSendDate(),
                message.getText(),
                attachments.size() != 0,
                getAttachmentName(attachments),
                message.getAuthorId()
        );
    }

    /**
     * Метод для получения имени последнего вложения из списка AttachmentEntity.
     *
     * @param attachments список объектов AttachmentEntity, из которых будет получено имя последнего вложения
     * @return имя последнего вложения или null, если список пуст
     */
    private String getAttachmentName(List<AttachmentEntity> attachments){
        int size = attachments.size();
        if(size>0){
            return attachments.get(attachments.size() -1).getName();
        }
        else return null;
    }

}
