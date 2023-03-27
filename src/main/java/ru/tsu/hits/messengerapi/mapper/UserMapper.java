package ru.tsu.hits.messengerapi.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.tsu.hits.messengerapi.dto.SignUpDto;
import ru.tsu.hits.messengerapi.dto.UserDto;
import ru.tsu.hits.messengerapi.entity.UserEntity;

import java.time.LocalDate;
import java.util.Date;

@Component
@RequiredArgsConstructor
public class UserMapper {
    public UserDto userToUserDto(UserEntity user) {
        return new UserDto(
                user.getId(),
                user.getLogin(),
                user.getName(),
                user.getBirthDate(),
                user.getCreationDate(),
                user.getSurname(),
                user.getPatronymic(),
                user.getSex()
        );
    }

    public UserEntity SignUpDtoToUser(SignUpDto signUpDto) {
        return UserEntity
                .builder()
                .login(signUpDto.getLogin())
                .sex(signUpDto.getSex())
                .name(signUpDto.getName())
                .surname(signUpDto.getSurname())
                .birthDate(signUpDto.getBirthDate())
                .patronymic(signUpDto.getPatronymic())
                .creationDate(convertToDateViaSqlDate(LocalDate.now()))
                .password(signUpDto.getPassword())
                .build();
    }

    private Date convertToDateViaSqlDate(LocalDate dateToConvert) {
        return java.sql.Date.valueOf(dateToConvert);
    }
}
