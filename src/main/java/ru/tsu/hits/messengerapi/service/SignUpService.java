package ru.tsu.hits.messengerapi.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.tsu.hits.messengerapi.dto.SignUpDto;
import ru.tsu.hits.messengerapi.dto.UserDto;
import ru.tsu.hits.messengerapi.entity.UserEntity;
import ru.tsu.hits.messengerapi.mapper.UserMapper;
import ru.tsu.hits.messengerapi.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class SignUpService {

    private final UserMapper userMapper;
    private final UserRepository userRepository;
    private final CheckLoginService checkLoginService;

    /**
     * Метод для регистрации пользователя.
     *
     * @param signUpDto информация для регистрации внешнего пользователя.
     */
    public UserDto userSignUp(SignUpDto signUpDto) {
        checkLoginService.existsByLogin(signUpDto.getLogin());
        UserEntity user = userMapper.SignUpDtoToUser(signUpDto);
        user = userRepository.save(user);
        return userMapper.userToUserDto(user);
    }
}
