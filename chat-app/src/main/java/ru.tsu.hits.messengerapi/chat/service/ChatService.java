package ru.tsu.hits.messengerapi.chat.service;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.tsu.hits.messengerapi.chat.dto.*;
import ru.tsu.hits.messengerapi.chat.entity.ChatEntity;
import ru.tsu.hits.messengerapi.chat.entity.MessageEntity;
import ru.tsu.hits.messengerapi.chat.mapper.ChatMapper;
import ru.tsu.hits.messengerapi.chat.repository.ChatRepository;
import ru.tsu.hits.messengerapi.chat.repository.ChatUserRepository;
import ru.tsu.hits.messengerapi.chat.repository.MessageRepository;
import ru.tsu.hits.messengerapi.common.exception.BadRequestException;
import ru.tsu.hits.messengerapi.common.exception.ForbiddenException;
import ru.tsu.hits.messengerapi.common.exception.NotFoundException;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Сервис для работы с чатом.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ChatService {

    private final ChatMapper chatMapper;
    private final ChatRepository chatRepository;
    private final ChatUserRepository chatUserRepository;
    private final MessageRepository messageRepository;
    private final IntegrationRequestService integrationRequestService;
    private final ChatMessageService chatMessageService;


    /**
     * Создает новый чат на основе переданных данных, сохраняет его в репозитории
     * и возвращает объект ChatDto с информацией о созданном чате.
     *
     * @param createChatDto объект, содержащий данные для создания чата
     * @param targetUserId  идентификатор пользователя, с которым создается диалог
     * @return объект ChatDto с информацией о созданном чате
     */
    @Transactional
    public ChatDto createChat(CreateChatDto createChatDto, UUID targetUserId) {
        checkUsers(createChatDto); //проверка кол-ва юзеров
        checkExternalUsers(createChatDto.getUsersId(), targetUserId); //есть ли в друзьях, нет ли в черном списке
        integrationRequestService.getFileData(createChatDto.getAvatarId()); //проверка существует ли файл с таким id в файловом хранилище
        ChatEntity chatEntity = chatMapper.createChat(createChatDto, targetUserId);
        chatRepository.save(chatEntity);
        createChatUserEntities(createChatDto.getUsersId(), chatEntity.getId());
        return chatMapper.chatToChatDto(chatEntity);
    }

    /**
     * Возвращает объект ChatDto с информацией о чате с заданным идентификатором, если пользователь имеет к нему доступ.
     *
     * @param chatId       идентификатор чата
     * @param targetUserId идентификатор пользователя, который запрашивает информацию о чате
     * @return объект ChatDto с информацией о чате
     * @throws ForbiddenException если пользователь не имеет доступа к запрашиваемому чату
     * @throws NotFoundException  если чат не найден
     */
    @Transactional(readOnly = true)
    public ChatDto getChatInfo(UUID chatId, UUID targetUserId) {

        chatMessageService.findChat(chatId);
        chatMessageService.checkTargetUser(chatId, targetUserId);
        return chatMapper.chatToChatDto(chatMessageService.findChat(chatId));
    }

    /**
     * Обновляет информацию о чате на основе переданных данных, сохраняет изменения
     * в репозитории и возвращает объект ChatDto с обновленной информацией.
     *
     * @param updateChatInfoDto объект, содержащий данные для обновления информации о чате
     * @param targetUserId      идентификатор пользователя, который обновляет информацию о чате
     * @return объект ChatDto с обновленной информацией о чате
     * @throws ForbiddenException если пользователь не имеет доступа к обновляемому чату
     * @throws NotFoundException  если чат не найден
     */
    @Transactional
    public ChatDto updateChatInfo(UpdateChatInfoDto updateChatInfoDto, UUID targetUserId) {

        chatMessageService.checkTargetUser(updateChatInfoDto.getId(), targetUserId);  //есть ли доступ у пользователя к чату
        checkExternalUsers(updateChatInfoDto.getUsersId(), targetUserId);  //проверка есть ли в друзьях
        integrationRequestService.getFileData(updateChatInfoDto.getAvatarId());//проверка существует ли файл с таким id в файловом хранилище
        ChatEntity chatEntity = chatMessageService.findChat(updateChatInfoDto.getId());
        checkAdmin(updateChatInfoDto.getUsersId(), chatEntity.getAdmin()); //проверка не удаляют ли админа
        chatEntity.setName(updateChatInfoDto.getName());
        chatEntity.setAvatarId(updateChatInfoDto.getAvatarId());
        chatRepository.save(chatEntity);

        chatUserRepository.deleteChatUserEntitiesByChatId(chatEntity.getId());
        createChatUserEntities(updateChatInfoDto.getUsersId(), chatEntity.getId());
        return chatMapper.chatToChatDto(chatEntity);
    }

    /**
     * Получает дату отправки последнего сообщения в чате с указанным идентификатором и адресатом.
     *
     * @param chatId       идентификатор чата
     * @param targetUserId идентификатор адресата
     * @return дата отправки последнего сообщения или LocalDateTime.MIN, если сообщений нет
     */
    @Transactional(readOnly = true)
    public MessageEntity getLastMessage(UUID chatId, UUID targetUserId) {
        ChatEntity chat = chatMessageService.findChat(chatId);
        chatMessageService.checkTargetUser(chatId, targetUserId);
        return messageRepository.getFirstByChatOrderBySendDateDesc(chat);
    }

    /**
     * Возвращает страницу чата, содержащую список чатов, в которых участвует пользователь с указанным идентификатором.
     *
     * @param pageDto      объект, содержащий информацию о текущей странице
     * @param targetUserId идентификатор пользователя, для которого нужно получить список чатов
     * @return объект ChatPageDto, содержащий список чатов для указанного пользователя
     */
    @Transactional(readOnly = true)
    public ChatPageDto getChatPage(PageDto pageDto, UUID targetUserId) {

        List<ExtendedChatDto> chatDtos = getExtendedChatDtos(              //достаем id чатов, в которых состоит пользователь
                chatMessageService.getTargetUserChats(targetUserId),
                targetUserId
        );

        List<ExtendedChatDto> sortedChatDtos = sortByLastMessageSendDate(   //сортируем и фильтруем
                filterChatDtoList(
                        pageDto.getChatNameFilter(),
                        chatDtos)
        );

        return new ChatPageDto(pageDto, getPage(pageDto.getPageableDto().getPageSize(),
                pageDto.getPageableDto().getPageNumber(), sortedChatDtos));
    }

    /**
     * Фильтрует список объектов ExtendedChatDto на основе заданной строки фильтра.
     *
     * @param filter   строка, представляющая фильтр, который должен быть применен к списку chatDtos
     * @param chatDtos список объектов ExtendedChatDto, которые должны быть отфильтрованы
     * @return список объектов ExtendedChatDto, отфильтрованных в соответствии с заданной строкой фильтра
     */
    private List<ExtendedChatDto> filterChatDtoList(String filter, List<ExtendedChatDto> chatDtos) {
        List<ExtendedChatDto> filteredChatDtos;
        if (filter != null) {
            filteredChatDtos = chatDtos.stream()
                    .filter(
                            chat -> chat.getName().toLowerCase()
                                    .contains(filter.toLowerCase())
                    ).collect(Collectors.toList());
        } else {
            filteredChatDtos = chatDtos;
        }
        return filteredChatDtos;
    }

    /**
     * Находит чат по id и мапит сущность в дто.
     *
     * @param chats        список чатов, которые должны быть отображены
     * @param targetUserId UUID, представляющий идентификатор целевого пользователя
     * @return список объектов ExtendedChatDto, отображенных из предоставленных идентификаторов чатов
     */
    private List<ExtendedChatDto> getExtendedChatDtos(List<ChatEntity> chats, UUID targetUserId) {

        List<ExtendedChatDto> chatDtos = new ArrayList<>();
        for (ChatEntity chat : chats) {
            MessageEntity message = getLastMessage(chat.getId(), targetUserId);
            if (message == null) {
                chatDtos.add(chatMapper.chatToChatDateDto(chat));
            } else {
                chatDtos.add(chatMapper.chatToChatDateDto(chat, message));
            }
        }
        return chatDtos;
    }

    /**
     * Сортирует список объектов ExtendedChatDto по дате последнего отправленного сообщения в порядке убывания.
     *
     * @param chatDtos список объектов ExtendedChatDto, которые должны быть отсортированы
     * @return список объектов ExtendedChatDto, отсортированных по дате последнего
     * отправленного сообщения в порядке убывания
     */
    private List<ExtendedChatDto> sortByLastMessageSendDate(List<ExtendedChatDto> chatDtos) {

        Comparator<ExtendedChatDto> sortByDate =
                Comparator.comparing(
                        ExtendedChatDto::getLastMessageSendDate
                ).reversed();
        chatDtos.sort(sortByDate);
        return chatDtos;
    }

    /**
     * Метод для пагинации списка чатов
     *
     * @param size       размер страницы
     * @param pageNumber номер желаемой страницы
     * @param chatDtos   список объектов ExtendedChatDto
     */
    private List<ExtendedChatDto> getPage(int size, int pageNumber, List<ExtendedChatDto> chatDtos) {
        var start = pageNumber * size;
        var finish = start + size;
        if (finish >= chatDtos.size()) {
            finish = chatDtos.size();
        }
        return chatDtos.subList(start, finish);
    }

    /**
     * Создает записи о принадлежности пользователей к чату.
     *
     * @param uuids        идентификаторы пользователей, включая создателя чата
     * @param chatId       идентификатор созданного чата
     */
    @Transactional
    public void createChatUserEntities(List<UUID> uuids, UUID chatId) {
        for (UUID chatUserId : uuids) {
            chatUserRepository.save(chatMapper.createChatUserEntity(chatId, chatUserId));
        }
    }

    /**
     * Метод проверяет, является ли пользователь с идентификатором adminId администратором чата.
     * @param list список идентификаторов пользователей чата
     * @param adminId идентификатор пользователя, которого нужно проверить на наличие прав администратора
     * @throws  ForbiddenException, если пользователь не является администратором
     */
    private void checkAdmin(List<UUID> list, UUID adminId){
        if (!list.contains(adminId)){
            throw new ForbiddenException("Вы не можете удалить админа из чата.");
        }
    }
    
    /**
     * Проверяет, что в DTO для создания чата указано более одного пользователя.
     *
     * @param createChatDto DTO, содержащий информацию о создаваемом чате.
     * @throws BadRequestException если в DTO указан только один пользователь.
     */
    private void checkUsers(CreateChatDto createChatDto) {

        if (createChatDto.getUsersId().size() <= 1) {
            throw new BadRequestException("Вы не можете создать чат с одним пользователем. Перейдите к диалогу");
        }
    }

    /**
     * Проверяет список идентификаторов внешних пользователей
     * на корректность с помощью сервиса chatIntegrationRequestService,
     * вызывая метод checkId() для каждого идентификатора.
     *
     * @param externalUsersId список идентификаторов внешних пользователей
     */
    private void checkExternalUsers(List<UUID> externalUsersId, UUID targetUserId) {

        externalUsersId.remove(targetUserId);
        for (UUID externalUserId : externalUsersId) {
            integrationRequestService.isBlockedUser(externalUserId, targetUserId);
            integrationRequestService.isAFriend(externalUserId, targetUserId);
        }
    }
}
