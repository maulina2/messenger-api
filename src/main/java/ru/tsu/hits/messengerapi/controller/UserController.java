package ru.tsu.hits.messengerapi.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.tsu.hits.messengerapi.dto.UserDto;
import ru.tsu.hits.messengerapi.service.UserService;

import java.util.UUID;


@RestController
@RequestMapping("/api/v1/account")
@RequiredArgsConstructor
@Tag(name = "Личный кабинет")
public class UserController {

    private final UserService userService;

    @Operation(
            summary = "Получить общую информацию о пользователе.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @GetMapping("/{id}")
    UserDto getUserInfo(@RequestBody @PathVariable("id") UUID id) {

        return userService.getUserInfo(id);
    }

//        @PutMapping("/email")
//        UserDto editUserInfo(@Valid @RequestBody EditUserInfoDto editEmailDto) {
//
//            return userInfoService.updateEmail(id, editEmailDto);
//        }
}

