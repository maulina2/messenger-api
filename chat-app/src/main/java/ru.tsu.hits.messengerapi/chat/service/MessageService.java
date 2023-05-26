package ru.tsu.hits.messengerapi.chat.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.tsu.hits.messengerapi.chat.dto.*;
import ru.tsu.hits.messengerapi.chat.entity.AttachmentEntity;
import ru.tsu.hits.messengerapi.chat.entity.ChatEntity;
import ru.tsu.hits.messengerapi.chat.entity.ChatUserEntity;
import ru.tsu.hits.messengerapi.chat.entity.MessageEntity;
import ru.tsu.hits.messengerapi.chat.enumeration.ChatType;
import ru.tsu.hits.messengerapi.chat.mapper.AttachmentMapper;
import ru.tsu.hits.messengerapi.chat.mapper.ChatMapper;
import ru.tsu.hits.messengerapi.chat.mapper.MessageMapper;
import ru.tsu.hits.messengerapi.chat.repository.AttachmentRepository;
import ru.tsu.hits.messengerapi.chat.repository.ChatRepository;
import ru.tsu.hits.messengerapi.chat.repository.ChatUserRepository;
import ru.tsu.hits.messengerapi.chat.repository.MessageRepository;
import ru.tsu.hits.messengerapi.common.dto.FileDto;
import ru.tsu.hits.messengerapi.common.dto.NewNotificationDto;
import ru.tsu.hits.messengerapi.common.enumeration.NotificationType;
import ru.tsu.hits.messengerapi.common.util.RabbitMQBindings;

import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class MessageService {

    private final ChatMessageService chatMessageService;
    private final ChatRepository chatRepository;
    private final ChatMapper chatMapper;
    private final ChatUserRepository chatUserRepository;
    private final MessageRepository messageRepository;
    private final MessageMapper messageMapper;
    private final IntegrationRequestService integrationRequestService;
    private final AttachmentMapper attachmentMapper;
    private final AttachmentRepository attachmentRepository;
    private final StreamBridge streamBridge;

    /**
     * Отправляет сообщение в чат.
     *
     * @param sendMessageInChatDto объект с информацией о сообщении
     * @param targetUserId         идентификатор пользователя, которому отправляется сообщение
     * @return объект с информацией о сообщении в виде MessageDto
     */
    @Transactional
    public MessageDto sendMessageToChat(SendMessageInChatDto sendMessageInChatDto, UUID targetUserId) {

        ChatEntity chat = chatMessageService.findChat(sendMessageInChatDto.getChatId());
        chatMessageService.checkTargetUser(chat.getId(), targetUserId);
        MessageEntity message = messageMapper.createMessage(sendMessageInChatDto, chat, targetUserId);
        messageRepository.save(message);
        return messageMapper.messageToMessageDto(
                message,
                targetUserId,
                getAttachmentDtos(sendMessageInChatDto.getFileIds(), message)
        );
    }

    /**
     * Отправляет сообщение в диалог.
     *
     * @param sendMessageInDialogDto объект с информацией о сообщении
     * @param targetUserId           идентификатор пользователя, которому отправляется сообщение
     * @return объект с информацией о сообщении в виде MessageDto
     */
    @Transactional
    public MessageDto sendMessageToDialog(SendMessageInDialogDto sendMessageInDialogDto, UUID targetUserId) {

        UUID externalUserId = sendMessageInDialogDto.getExternalUserId();
        integrationRequestService.isAFriend(externalUserId, targetUserId);
        integrationRequestService.isBlockedUser(externalUserId, targetUserId);

        Optional<List<ChatUserEntity>> externalUserChats = chatUserRepository.findByUserId(externalUserId);
        Optional<List<ChatUserEntity>> targetUserChats = chatUserRepository.findByUserId(targetUserId);
        ChatEntity chat = getChat(externalUserChats, targetUserChats, targetUserId, externalUserId);

        MessageEntity message = messageMapper.createMessage(sendMessageInDialogDto, chat, targetUserId);
        messageRepository.save(message);
        sendNewMessageNotification(message, externalUserId);
        return messageMapper.messageToMessageDto(
                message,
                targetUserId,
                getAttachmentDtos(sendMessageInDialogDto.getFileIds(), message)
        );
    }

    /**
     * Получает список сообщений из чата.
     *
     * @param chatId       идентификатор чата
     * @param targetUserId идентификатор пользователя, для которого запрашиваются сообщения
     * @return список объектов с информацией о сообщениях в виде MessageDto
     */
    @Transactional(readOnly = true)
    public List<MessageDto> getChatMessages(UUID chatId, UUID targetUserId) {
        ChatEntity chat = chatMessageService.findChat(chatId);
        chatMessageService.checkTargetUser(chatId, targetUserId);
        List<MessageEntity> messages = messageRepository.getAllByChatOrderBySendDateDesc(chat);

        List<MessageDto> messageDtos = new ArrayList<>();

        for (MessageEntity message : messages) {
            messageDtos.add(messageMapper.messageToMessageDto(
                    message,
                    targetUserId,
                    mapAttachmentDtos(attachmentRepository.findByMessage(message))
            ));
        }
        return messageDtos;
    }

    /**
     * Получает чат, в котором участвуют два заданных пользователя, или создает новый диалог между ними.
     *
     * @param externalUserChats список всех чатов, в которых участвует первый пользователь
     * @param targetUserChats   список всех чатов, в которых участвует второй пользователь
     * @param targetUserId      идентификатор второго пользователя
     * @param externalUserId    идентификатор первого пользователя
     * @return найденный или созданный чат в виде ChatEntity
     */
    private ChatEntity getChat(Optional<List<ChatUserEntity>> externalUserChats,
                               Optional<List<ChatUserEntity>> targetUserChats,
                               UUID targetUserId,
                               UUID externalUserId) {

        if (externalUserChats.isPresent() && targetUserChats.isPresent()) {

            for (ChatUserEntity chatUser : externalUserChats.get()) {
                for (ChatUserEntity chatUser2 : targetUserChats.get()) {
                    if (chatUser.getChatId().equals(chatUser2.getChatId())) {
                        Optional<ChatEntity> chat = chatRepository.findById(chatUser.getChatId());
                        if (chat.isPresent()) {
                            if (chat.get().getChatType() == ChatType.DIALOG) {
                                return chat.get();
                            }
                        }
                    }
                }
            }
        }
        return createChat(externalUserId, targetUserId);
    }

    /**
     * Ищет сообщения, содержащие определенную строку, в чатах целевого пользователя.
     *
     * @param searchMessageDto DTO, содержащий строку поиска.
     * @param targetUserId     Идентификатор целевого пользователя.
     * @return Список объектов FoundMessageDto, представляющих найденные сообщения.
     */
    @Transactional(readOnly = true)
    public List<FoundMessageDto> searchMessage(SearchMessageDto searchMessageDto, UUID targetUserId) {

        List<FoundMessageDto> foundMessageDtos = new ArrayList<>();
        List<ChatEntity> chats = chatMessageService.getTargetUserChats(targetUserId);

        for (ChatEntity chat : chats) {
            List<MessageEntity> filteredMessages = filterMessages
                    (
                            searchMessageDto.getSearchString(),
                            messageRepository.getAllByChatId(chat.getId())
                    );

            for (MessageEntity message : filteredMessages) {
                foundMessageDtos.add(messageMapper.messageToFoundMessageDto(chat, message, attachmentRepository.findByMessage(message)));
            }
        }
        Comparator<FoundMessageDto> sortByDate =
                Comparator.comparing(
                        FoundMessageDto::getMessageSendDate
                ).reversed();
        foundMessageDtos.sort(sortByDate);

        return foundMessageDtos;
    }

    /**
     * Фильтрует список сообщений, проверяя, содержит ли их текст определенную строку.
     *
     * @param filter   Строка для фильтрации.
     * @param messages Список сообщений для фильтрации.
     * @return Отфильтрованный список объектов MessageEntity.
     */
    private List<MessageEntity> filterMessages(String filter, List<MessageEntity> messages) {

        List<MessageEntity> messageEntities;
        messageEntities = messages.stream()
                .filter(
                        message -> message.getText().toLowerCase()
                                .contains(filter.toLowerCase())
                ).collect(Collectors.toList());
        return messageEntities;
    }

    /**
     * Создает диалог между двумя пользователями.
     *
     * @param externalUserId идентификатор одного из пользователей
     * @param targetUserId   идентификатор другого пользователя
     * @return созданный диалог в виде ChatEntity
     */
    @Transactional
    public ChatEntity createChat(UUID externalUserId, UUID targetUserId) {

        ChatEntity chat = chatMapper.createDialog(
                integrationRequestService.getFullName(externalUserId).getFullName()
        );
        chatRepository.save(chat);
        chatUserRepository.save(chatMapper.createChatUserEntity(chat.getId(), targetUserId));
        chatUserRepository.save(chatMapper.createChatUserEntity(chat.getId(), externalUserId));
        return chat;
    }

    /**
     * Метод отправляет интеграционный запрос в файловый сервис и получает оттуда информацию о файле.
     *
     * @param fileIds список идентификаторов файлов
     * @param message сообщение, к которому прикреплены файлы
     * @return список AttachmentDto
     */
    @Transactional(readOnly = true)
    public List<AttachmentDto> getAttachmentDtos(List<UUID> fileIds, MessageEntity message) {

        if (fileIds == null || fileIds.isEmpty()) {
            return null;
        }
        List<FileDto> fileDtos = new ArrayList<>();
        for (UUID attachmentId : fileIds) {
            fileDtos.add(integrationRequestService.getFileData(attachmentId));
        }

        List<AttachmentEntity> attachmentEntities = new ArrayList<>();
        for (FileDto fileDto : fileDtos) {
            attachmentEntities.add(attachmentMapper.attachmentDtoToEntity(message, fileDto));
        }
        attachmentRepository.saveAll(attachmentEntities);

        return mapAttachmentDtos(attachmentEntities);
    }

    /**
     * Метод получает список AttachmentEntity и преобразует его в список AttachmentDto с помощью attachmentMapper.
     *
     * @param attachmentEntities список AttachmentEntity
     * @return список AttachmentDto
     */
    private List<AttachmentDto> mapAttachmentDtos(List<AttachmentEntity> attachmentEntities) {

        List<AttachmentDto> attachmentDtos = new ArrayList<>();
        for (AttachmentEntity attachment : attachmentEntities) {
            attachmentDtos.add(attachmentMapper.attachmentEntityToDto(attachment));
        }
        return attachmentDtos;
    }

    /**
     * Отправляет уведомление о новом сообщении пользователю с указанным внешним идентификатором.
     *
     * @param message объект сообщения, для которого отправляется уведомление
     * @param externalUserId внешний идентификатор пользователя, которому отправляется уведомление
     */
    private void sendNewMessageNotification(MessageEntity message, UUID externalUserId) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        NewNotificationDto newNotificationDto = NewNotificationDto.builder()
                .targetUserId(externalUserId)
                .type(NotificationType.NEW_MESSAGE)
                .message("Вам пришло новое сообщение от пользователя " +
                        integrationRequestService.getFullName(message.getAuthorId()).getFullName()
                        + ". Время отправки сообщения: " + message.getSendDate().format(formatter) +
                        ". Текст сообщения: "+ message.getText()
                        .substring(0, Math.min(message.getText().length(), 100)) + ".")
                .build();
        streamBridge.send(RabbitMQBindings.CREATE_NOTIFICATION_OUT, newNotificationDto);
    }

}
