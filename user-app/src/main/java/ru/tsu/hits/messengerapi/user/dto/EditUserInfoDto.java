package ru.tsu.hits.messengerapi.user.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.UUID;

/**
 * Дто в которой передается информация для изменения данных текущего пользователя.
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class EditUserInfoDto {

    @NotNull @NotBlank(message = "ФИО пользователя не может быть пустым.")
    private String fullName;

    private LocalDate birthDate;

    private String phoneNumber;

    private String city;

    private UUID avatar;

}
