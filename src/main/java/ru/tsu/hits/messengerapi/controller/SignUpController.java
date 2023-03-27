package ru.tsu.hits.messengerapi.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.tsu.hits.messengerapi.dto.SignUpDto;
import ru.tsu.hits.messengerapi.dto.UserDto;
import ru.tsu.hits.messengerapi.service.SignUpService;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@Tag(name = "Регистрация")
public class SignUpController {
    private final SignUpService signUpService;

    @Operation(summary = "Регистрация пользователя.")
    @PostMapping("/sign-up")
    public UserDto userSignUp(@Valid @RequestBody SignUpDto signUpDto) {
        return signUpService.userSignUp(signUpDto);
    }
}
