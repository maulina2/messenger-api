package ru.tsu.hits.messengerapi.user.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.tsu.hits.messengerapi.common.dto.UserDto;
import ru.tsu.hits.messengerapi.user.dto.FullUserDto;
import ru.tsu.hits.messengerapi.user.dto.SignInDto;
import ru.tsu.hits.messengerapi.user.service.SignInService;

import javax.validation.Valid;


/**
 * Контроллер для обработки запроса на аутентификацию.
 */
@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@Tag(name = "Аутентификация")
public class SignInController {

    private final SignInService signInService;

    /**
     * Метод userSignIn аутентифицирует пользователя на основе переданного объекта типа SignInDto.
     *
     * @param signInDto объект типа SignInDto, содержащий данные для аутентификации пользователя.
     * @return jwt токен
     */
    @Operation(summary = "Аутентификация пользователя.")
    @PostMapping("/sign-in")
    public ResponseEntity<UserDto> signIn(@Valid @RequestBody SignInDto signInDto) {
        FullUserDto fullUserDto = signInService.signIn(signInDto);
        HttpHeaders header = new HttpHeaders();
        header.set("JwtToken", fullUserDto.getJwtToken());
        return new ResponseEntity<>(fullUserDto.getUserDto(), header, HttpStatus.OK);
    }
}
