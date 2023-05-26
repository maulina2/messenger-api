package ru.tsu.hits.messengerapi.friends.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.tsu.hits.messengerapi.common.dto.IsBlockedUserDto;
import ru.tsu.hits.messengerapi.friends.service.BlackListIntegrationRequestService;
import ru.tsu.hits.messengerapi.friends.service.BlackListService;

import java.util.UUID;

/**
 * Контроллер для обработки интеграционных запросов в черном списке.
 */
@RestController
@RequestMapping("/integration/friends/black-list")
@RequiredArgsConstructor
@Tag(name = "Интеграционные запросы в черном списке")
public class BlackListIntegrationRequestController {
    private final BlackListService blackListService;

    private final BlackListIntegrationRequestService blackListIntegrationRequestService;

    /**
     * Проверяет, находится ли пользователь в черном списке.
     *
     * @param isBlockedUserDto дто;
     * @return true, если пользователь находится в черном списке, false - если нет.
     */
    @Operation(summary = "Проверка на нахождение пользователя в черном списке.")
    @PostMapping("/is_blocked")
    public boolean isBlockedUser(@RequestBody IsBlockedUserDto isBlockedUserDto) {
        return blackListService.isBlockedUser(isBlockedUserDto.getExternalUserId(), isBlockedUserDto.getTargetUserId());
    }

    /**
     * Метод для синхронизации данных друзей пользователя с user-app.
     *
     * @param id идентификатор пользователя, чьи данные нужно синхронизировать с другими сервисами.
     */
    @Operation(summary = "Синхронизация данных")
    @PatchMapping("{id}")
    public void dataSynchronization(@PathVariable UUID id) {
        blackListIntegrationRequestService.dataSynchronization(id);
    }
}
