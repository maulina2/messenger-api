package ru.tsu.hits.messengerapi.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.tsu.hits.messengerapi.dto.EditUserInfoDto;
import ru.tsu.hits.messengerapi.dto.UserDto;
import ru.tsu.hits.messengerapi.service.UserService;

import java.util.UUID;


@RestController
@RequestMapping("/api/v1/account")
@RequiredArgsConstructor
@Tag(name = "Личный кабинет")
public class UserController {

    private final UserService userService;

    @Operation(summary = "Получить информацию о пользователе.")
    @GetMapping("/{id}")
    UserDto getUserInfo(@RequestBody @PathVariable("id") UUID id) {

        return userService.getUserInfo(id);
    }

    @Operation(summary = "Изменить информацию о пользователе.")
    @PutMapping("/{id}")
    UserDto editUserInfo(@RequestBody @PathVariable("id") UUID id,
                         EditUserInfoDto editUserInfoDto) {

        return userService.updateUserInfo(id, editUserInfoDto);
    }
}

