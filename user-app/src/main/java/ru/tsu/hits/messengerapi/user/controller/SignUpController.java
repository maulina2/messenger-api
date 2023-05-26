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
import ru.tsu.hits.messengerapi.user.dto.SignUpDto;
import ru.tsu.hits.messengerapi.user.service.SignUpService;

import javax.validation.Valid;

/**
 * Контроллер для обработки запроса на регистрацию.
 */
@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@Tag(name = "Регистрация")
public class SignUpController {
    private final SignUpService signUpService;

    /**
     * Метод userSignUp регистрирует нового пользователя на основе переданного объекта типа SignUpDto.
     *
     * @param signUpDto объект типа SignUpDto, содержащий данные для регистрации пользователя
     * @return jwt токен
     */
    @Operation(summary = "Зарегистрировать пользователя.")
    @PostMapping("/sign-up")
    ResponseEntity<UserDto> userSignUp(@Valid @RequestBody SignUpDto signUpDto) {

        FullUserDto fullUserDto = signUpService.userSignUp(signUpDto);
        HttpHeaders header = new HttpHeaders();
        header.set("JwtToken", fullUserDto.getJwtToken());
        return new ResponseEntity<>(fullUserDto.getUserDto(), header, HttpStatus.OK);
    }

}
