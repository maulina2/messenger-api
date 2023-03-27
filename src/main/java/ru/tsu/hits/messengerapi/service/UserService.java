package ru.tsu.hits.messengerapi.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.tsu.hits.messengerapi.dto.UserDto;
import ru.tsu.hits.messengerapi.entity.UserEntity;
import ru.tsu.hits.messengerapi.exception.NotFoundException;
import ru.tsu.hits.messengerapi.mapper.UserMapper;
import ru.tsu.hits.messengerapi.repository.UserRepository;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserDto getUserInfo(UUID id) {
        UserEntity user = getUserById(id);

        return userMapper.userToUserDto(user);
    }

    private UserEntity getUserById(UUID id) {
        return userRepository
                .findById(id)
                .orElseThrow(() -> {
                    throw new NotFoundException("Не авторизован.");
                });
    }
}
