package ru.tsu.hits.messengerapi.friends.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.tsu.hits.messengerapi.common.dto.IsAFriendDto;
import ru.tsu.hits.messengerapi.friends.service.FriendIntegrationRequestService;

import java.util.UUID;

/**
 * Контроллер для обработки интеграционных запросов в друзьях.
 */
@RestController
@RequestMapping("/integration/friends")
@RequiredArgsConstructor
@Tag(name = "Интеграционные запросы в друзьях")
public class FriendIntegrationRequestController {

    private final FriendIntegrationRequestService friendIntegrationRequestService;

    /**
     * Метод для синхронизации данных друзей пользователя с user-app.
     *
     * @param id идентификатор пользователя, чьи данные нужно синхронизировать с другими сервисами.
     */
    @Operation(summary = "Синхронизация данных")
    @PatchMapping("{id}")
    public void dataSynchronization(@PathVariable UUID id) {
        friendIntegrationRequestService.dataSynchronization(id);
    }

    @Operation(summary = "Проверка на нахождение пользователя в друзьях.")
    @PostMapping("/is_friend")
    public boolean isAFriend(@RequestBody IsAFriendDto isAFriendDto) {
        return friendIntegrationRequestService.isAFriend(isAFriendDto);
    }
}
