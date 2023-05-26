package ru.tsu.hits.messengerapi.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.tsu.hits.messengerapi.commonsecurity.service.JwtService;
import ru.tsu.hits.messengerapi.user.dto.FullUserDto;
import ru.tsu.hits.messengerapi.user.dto.SignUpDto;
import ru.tsu.hits.messengerapi.user.entity.UserEntity;
import ru.tsu.hits.messengerapi.user.mapper.UserMapper;
import ru.tsu.hits.messengerapi.user.repository.UserRepository;

/**
 * Сервис для обработки запроса на регистрацию пользователя.
 */
@Service
@RequiredArgsConstructor
public class SignUpService {

    private final UserMapper userMapper;
    private final UserRepository userRepository;
    private final JwtService jwtService;

    /**
     * Метод для регистрации пользователя.
     *
     * Сначала пользователь регистрируется без аватарки, затем при редактировании своего профиля может загрузить аватар.
     * @param signUpDto информация для регистрации внешнего пользователя.
     */
    @Transactional
    public FullUserDto userSignUp(SignUpDto signUpDto) {

        UserEntity user = userMapper.SignUpDtoToUser(signUpDto);
        user = userRepository.save(user);
        return new FullUserDto(
                userMapper.userToUserDto(user),
                jwtService.generateJwt(userMapper.userToJwtUserData(user))
        );
    }
}
