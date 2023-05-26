package ru.tsu.hits.messengerapi.chat.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import ru.tsu.hits.messengerapi.chat.dto.*;
import ru.tsu.hits.messengerapi.chat.service.MessageService;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

import static ru.tsu.hits.messengerapi.commonsecurity.service.JwtService.extractId;

/**
 * Контроллер для обработки запросов, связанных с сообщениями в чатах
 */
@RestController
@RequestMapping("/api/v1/chat/message")
@RequiredArgsConstructor
@Tag(name = "Сообщения в чате")
public class MessageController {

    private final MessageService messageService;

    /**
     * Метод для отправки сообщения в личный диалог.
     *
     * @param sendMessageInDialogDto объект, содержащий информацию о сообщении
     * @param authentication объект, содержащий информацию об аутентификации пользователя
     * @return объект MessageDto, содержащий информацию об отправленном сообщении
     */
    @Operation(
            summary = "Отправить сообщение в личный диалог.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @PostMapping("/dialog")
    public MessageDto sendMessageToDialog (@RequestBody @Valid SendMessageInDialogDto sendMessageInDialogDto, Authentication authentication) {
        return messageService.sendMessageToDialog(sendMessageInDialogDto, extractId(authentication));
    }

    /**
     * Метод для отправки сообщения в чат.
     *
     * @param sendMessageInChatDto объект, содержащий информацию о сообщении
     * @param authentication объект, содержащий информацию об аутентификации пользователя
     * @return объект MessageDto, содержащий информацию об отправленном сообщении
     */
    @Operation(
            summary = "Отправить сообщение в чат.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @PostMapping("/chat")
    public MessageDto sendMessageToChat (@RequestBody @Valid SendMessageInChatDto sendMessageInChatDto, Authentication authentication) {
        return messageService.sendMessageToChat(sendMessageInChatDto, extractId(authentication));
    }

    /**
     * Метод для получения списка сообщений в чате.
     *
     * @param chatId идентификатор чата
     * @param authentication объект, содержащий информацию об аутентификации пользователя
     * @return список объектов MessageDto, содержащих информацию о сообщениях в чате
     */
    @Operation(
            summary = "Просмотреть переписку.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @GetMapping("/{chatId}")
    public List<MessageDto> getChatMessages (@PathVariable UUID chatId, Authentication authentication) {
        return messageService.getChatMessages(chatId, extractId(authentication));
    }

    /**
     * Метод для поиска сообщений в чатах.
     *
     * @param searchMessageDto объект, содержащий строку поиска.
     * @param authentication объект, содержащий информацию об аутентификации пользователя
     * @return список объектов FoundMessageDto, содержащих информацию о найденных сообщениях
     */
    @Operation(
            summary = "Осуществить поиск сообщения в чатах",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @PostMapping("")
    public List<FoundMessageDto> searchMessage(@RequestBody @Valid SearchMessageDto searchMessageDto, Authentication authentication) {
        return messageService.searchMessage(searchMessageDto, extractId(authentication));
    }
}
