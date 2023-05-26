package ru.tsu.hits.messengerapi.notification.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import ru.tsu.hits.messengerapi.notification.dto.ChangeStatusNotificationDto;
import ru.tsu.hits.messengerapi.notification.dto.NotificationPageDto;
import ru.tsu.hits.messengerapi.notification.dto.PageDto;
import ru.tsu.hits.messengerapi.notification.service.NotificationService;

import javax.validation.Valid;

import static ru.tsu.hits.messengerapi.commonsecurity.service.JwtService.extractId;


/**
 * Контроллер для обработки запросов, связанных с чатом.
 */
@RestController
@RequestMapping("/api/v1/notification")
@RequiredArgsConstructor
@Tag(name = "Уведомления")
public class NotificationController {

    private final NotificationService notificationService;

    /**
     * Получает количество непрочитанных уведомлений для пользователя.
     *
     * @param authentication объект аутентификации пользователя.
     * @return количество непрочитанных уведомлений.
     */
    @Operation(
            summary = "Получить количество непрочитанных уведомлений.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @GetMapping("")
    public int getUnreadNotifications(Authentication authentication) {

        return notificationService.getUnreadNotifications(extractId(authentication));
    }

    /**
     * Меняет статус уведомлений (прочитано/непрочитано) для пользователя.
     *
     * @param changeStatusNotificationDto DTO, содержащий информацию о изменении статуса уведомлений.
     * @param authentication объект аутентификации пользователя.
     * @return количество измененных уведомлений.
     */
    @Operation(
            summary = "Пометить уведомления прочитанными/непрочитанными.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @PostMapping("/change_status")
    public int changeStatusNotifications(@RequestBody @Valid ChangeStatusNotificationDto changeStatusNotificationDto, Authentication authentication) {

        return notificationService.changeStatusNotifications(changeStatusNotificationDto, extractId(authentication));
    }

    /**
     * Получает список уведомлений для пользователя с пагинацией.
     *
     * @param pageDto DTO, содержащий информацию о странице списка уведомлений.
     * @param authentication объект аутентификации пользователя.
     * @return DTO, содержащий информацию о странице списка уведомлений.
     */
    @Operation(
            summary = "Получить список уведомлений.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @PostMapping("")
    public NotificationPageDto getNotifications(@RequestBody @Valid PageDto pageDto, Authentication authentication) {

        return notificationService.getNotifications(pageDto, extractId(authentication));
    }
}
