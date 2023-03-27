package ru.tsu.hits.messengerapi.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.tsu.hits.messengerapi.dto.EditUserInfoDto;
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

    public UserDto updateUserInfo(UUID id, EditUserInfoDto editUserInfoDto) {
        UserEntity user = getUserById(id);

        user.setBirthDate(editUserInfoDto.getBirthDate());
        user.setName(editUserInfoDto.getName());
        user.setPatronymic(editUserInfoDto.getPatronymic());
        user.setSurname(editUserInfoDto.getSurname());
        user.setPassword(editUserInfoDto.getPassword());
        user = userRepository.save(user);

        return userMapper.userToUserDto(user);
    }

    private UserEntity getUserById(UUID id) {
        return userRepository
                .findById(id)
                .orElseThrow(() -> {
                    throw new NotFoundException("Такой пользователь не найден.");
                });
    }
}
