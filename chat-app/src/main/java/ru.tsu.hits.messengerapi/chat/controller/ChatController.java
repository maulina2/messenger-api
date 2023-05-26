package ru.tsu.hits.messengerapi.chat.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import ru.tsu.hits.messengerapi.chat.dto.*;
import ru.tsu.hits.messengerapi.chat.service.ChatService;
import ru.tsu.hits.messengerapi.commonsecurity.dto.JwtUserData;

import javax.validation.Valid;
import java.util.UUID;

import static ru.tsu.hits.messengerapi.commonsecurity.service.JwtService.extractId;

/**
 * Контроллер для обработки запросов, связанных с чатом.
 */
@RestController
@RequestMapping("/api/v1/chat")
@RequiredArgsConstructor
@Tag(name = "Чат")
public class ChatController {
    private final ChatService chatService;

    /**
     * Создает новый чат на основе переданных данных и возвращает объект ChatDto с информацией о созданном чате.
     *
     * @param createChatDto объект, содержащий данные для создания чата
     * @param authentication объект, содержащий информацию об аутентифицированном пользователе
     * @return объект ChatDto с информацией о созданном чате
     */
    @Operation(
            summary = "Создать чат.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @PostMapping("")
    public ChatDto createChat(@RequestBody @Valid CreateChatDto createChatDto, Authentication authentication) {
        var userData = (JwtUserData) authentication.getPrincipal();
        return chatService.createChat(createChatDto, userData.getId());
    }

    /**
     * Возвращает объект ChatDto с информацией о чате с заданным идентификатором.
     *
     * @param id идентификатор чата
     * @param authentication объект, содержащий информацию об аутентифицированном пользователе
     * @return объект ChatDto с информацией о чате
     */
    @Operation(
            summary = "Просмотреть информацию о чате.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @GetMapping("/{id}")
    public ChatDto getChatInfo(@PathVariable UUID id, Authentication authentication) {
        return chatService.getChatInfo(id, extractId(authentication));
    }

    /**
     * Обновляет информацию о чате на основе переданных данных и возвращает объект ChatDto с обновленной информацией.
     *
     * @param updateChatInfoDto объект, содержащий данные для обновления информации о чате
     * @param authentication объект, содержащий информацию об аутентифицированном пользователе
     * @return объект ChatDto с обновленной информацией о чате
     */
    @Operation(
            summary = "Изменить информацию о чате.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @PutMapping("")
    public ChatDto updateChatInfo(@RequestBody @Valid UpdateChatInfoDto updateChatInfoDto, Authentication authentication) {
        return chatService.updateChatInfo(updateChatInfoDto, extractId(authentication));
    }

    /**
     * Метод для получения списка переписок.
     *
     * @param pageDto объект, содержащий информацию о запрашиваемой странице
     * @param authentication объект, содержащий информацию об аутентификации пользователя
     * @return объект ChatPageDto, содержащий список переписок и информацию о пагинации.
     */
    @Operation(
            summary = "Получить список переписок.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @PostMapping("/page")
    public ChatPageDto getChatPage(@RequestBody @Valid PageDto pageDto, Authentication authentication) {
        return chatService.getChatPage(pageDto, extractId(authentication));
    }
}
