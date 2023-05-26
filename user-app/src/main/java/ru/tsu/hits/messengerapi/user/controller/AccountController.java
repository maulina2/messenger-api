package ru.tsu.hits.messengerapi.user.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import ru.tsu.hits.messengerapi.common.dto.UserDto;
import ru.tsu.hits.messengerapi.user.dto.EditUserInfoDto;
import ru.tsu.hits.messengerapi.user.service.AccountService;

import javax.validation.Valid;

import static ru.tsu.hits.messengerapi.commonsecurity.service.JwtService.extractId;

/**
 * Контроллер для обработки запросов в личном кабинете пользователя.
 */
@RestController
@RequestMapping("/api/v1/users/account")
@RequiredArgsConstructor
@Tag(name = "Аккаунт пользователя")
public class AccountController {

    private final AccountService accountService;

    /**
     * Получает информацию о текущем пользователе на основе его аутентификационных данных.
     *
     * @param authentication объект, содержащий аутентификационные данные текущего пользователя.
     * @return объект UserDto, содержащий информацию о пользователе.
     */
    @Operation(
            summary = "Получить информацию о себе.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @GetMapping
    UserDto getInfo(Authentication authentication) {
       
        return accountService.getUserInfo(extractId(authentication));
    }

    /**
     * Обновляет информацию о текущем пользователе на основе переданных данных из объекта EditUserInfoDto.
     *
     * @param authentication объект, содержащий аутентификационные данные текущего пользователя
     * @param editUserInfoDto объект, содержащий данные для обновления информации о пользователе
     * @return объект UserDto, содержащий обновленную информацию о пользователе
     */
    @Operation(
            summary = "Изменить информацию о себе.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @PutMapping
    UserDto editUserInfo(Authentication authentication, @RequestBody @Valid EditUserInfoDto editUserInfoDto) {
       
        return accountService.editUserInfo(extractId(authentication), editUserInfoDto);
    }

}
