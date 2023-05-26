package ru.tsu.hits.messengerapi.user.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.tsu.hits.messengerapi.common.enumeration.Sex;
import ru.tsu.hits.messengerapi.user.util.annotation.CheckBirthDateValidation;
import ru.tsu.hits.messengerapi.user.util.annotation.UniqueEmailValidation;
import ru.tsu.hits.messengerapi.user.util.annotation.UniqueLoginValidation;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

/**
 * Дто,в которой передаются данные для регистрации пользователя.
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class SignUpDto {
    @NotBlank(message = "Логин пользователя не должен быть пустой.") @NotNull
    @UniqueLoginValidation
    private String login;

    @NotBlank(message = "ФИО пользователя не должно быть пустым.") @NotNull
    private String fullName;

    private String phoneNumber;

    private String city;

    @NotBlank(message = "Почта не может быть пустой.") @NotNull
    @Email(message = "Некорректный формат почты.")
    @UniqueEmailValidation
    private String email;

    @CheckBirthDateValidation
    private LocalDate birthDate;

    private String password;

    @Enumerated(EnumType.STRING)
    private Sex sex;
}
