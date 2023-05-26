package ru.tsu.hits.messengerapi.user.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import ru.tsu.hits.messengerapi.common.dto.UserDto;
import ru.tsu.hits.messengerapi.user.dto.UserPageDto;
import ru.tsu.hits.messengerapi.user.service.UserService;

import javax.validation.Valid;
import java.util.UUID;

import static ru.tsu.hits.messengerapi.commonsecurity.service.JwtService.extractId;

/**
 * Контроллер для обработки запросов на просмотр информации о других пользователях.
 */
@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@Tag(name = "Информация о других пользователях")
public class UserController {
    private final UserService userService;


    /**
     * Метод для получения информации о пользователе на основе его id.
     *
     * @param id - id пользователя, информацию о котором мы хотим получить.
     * @return объект UserDto, содержащий информацию о пользователе.
     */
    @Operation(
            summary = "Получить информацию о пользователе.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @GetMapping("{id}")
    UserDto getUserInfo(@PathVariable UUID id, Authentication authentication) {
        
        return userService.getExternalUserInfo(id, extractId(authentication));
    }

    /**
     * Метод для получения списка всех пользователей.
     *
     * @param userPageDto дто, в которой хранится информация для пагинации и фильтрации списка.
     * @return Page<UserDto> стрница, содержащая список пользователей.
     */
    @Operation(
            summary = "Получить список пользователей.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @PostMapping("")
    Page<UserDto> getUsers(@RequestBody @Valid UserPageDto userPageDto) {

        return userService.getUsersPage(userPageDto);
    }

}

