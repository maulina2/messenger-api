package ru.tsu.hits.messengerapi.user.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.tsu.hits.messengerapi.common.dto.FullNameDto;
import ru.tsu.hits.messengerapi.user.service.UserIntegrationRequestService;

import java.util.UUID;

/**
 * Контроллер для обработки интеграционных запросов.
 */
@RestController
@RequestMapping("/integration/users")
@RequiredArgsConstructor
@Tag(name = "Интеграционные запросы")
public class IntegrationRequestController {

    private final UserIntegrationRequestService userIntegrationRequestService;

    /**
     * Метод для получения ФИО пользователя на основе его id.
     *
     * @param id - id пользователя, информацию о котором мы хотим получить.
     * @return объект FullNameDto, содержащий информацию о пользователе.
     */
    @Operation(summary = "Получить ФИО пользователя")
    @GetMapping("{id}")
    FullNameDto getUserFullName(@PathVariable UUID id) {
        return userIntegrationRequestService.getUserFullName(id);
    }

}
