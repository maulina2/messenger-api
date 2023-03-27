package ru.tsu.hits.messengerapi.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.tsu.hits.messengerapi.enumeration.Sex;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class SignUpDto {
    private String login;

    private String name;

    private Date birthDate;

    private Date creationDate;

    private String surname;

    private String patronymic;

    private String password;

    @Enumerated(EnumType.STRING)
    private Sex sex;
}
