package ru.tsu.hits.messengerapi.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.tsu.hits.messengerapi.dto.UserDto;
import ru.tsu.hits.messengerapi.entity.UserEntity;

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
}
