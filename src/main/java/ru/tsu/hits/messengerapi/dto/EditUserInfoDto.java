package ru.tsu.hits.messengerapi.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class EditUserInfoDto {

    private String name;

    private LocalDate birthDate;

    private String surname;

    private String patronymic;

    private String password;
}
