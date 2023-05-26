package ru.tsu.hits.messengerapi.common.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.tsu.hits.messengerapi.common.enumeration.Sex;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.time.LocalDate;
import java.util.Date;
import java.util.UUID;

/**
 * Дто в которой передаются данные о пользователе.
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class UserDto {
    private UUID id;

    private String login;

    private String fullName;

    private LocalDate birthDate;

    private Date creationDate;

    private String email;

    private String phoneNumber;

    private String city;

    private UUID avatar;

    @Enumerated(EnumType.STRING)
    private Sex sex;
}
