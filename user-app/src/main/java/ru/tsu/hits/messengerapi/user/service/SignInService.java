package ru.tsu.hits.messengerapi.user.service;


import lombok.RequiredArgsConstructor;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.tsu.hits.messengerapi.common.dto.NewNotificationDto;
import ru.tsu.hits.messengerapi.common.enumeration.NotificationType;
import ru.tsu.hits.messengerapi.common.exception.UnauthorizedException;
import ru.tsu.hits.messengerapi.commonsecurity.service.JwtService;
import ru.tsu.hits.messengerapi.user.dto.FullUserDto;
import ru.tsu.hits.messengerapi.user.dto.SignInDto;
import ru.tsu.hits.messengerapi.user.entity.UserEntity;
import ru.tsu.hits.messengerapi.user.mapper.UserMapper;
import ru.tsu.hits.messengerapi.user.repository.UserRepository;

import java.util.UUID;

import static ru.tsu.hits.messengerapi.common.util.RabbitMQBindings.CREATE_NOTIFICATION_OUT;


/**
 * Сервис для аутентификации пользователя.
 */
@Service
@RequiredArgsConstructor
public class SignInService {

    private final UserMapper userMapper;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final StreamBridge streamBridge;

    /**
     * Метод, который аутентифицирует пользователя по логину и паролю.
     *
     * @param signInDto DTO с данными для авторизации (логин и пароль).
     * @return JWT-токен, сгенерированный для пользователя.
     * @throws UnauthorizedException если указаны неверные данные.
     */
    @Transactional(readOnly = true)
    public FullUserDto signIn(SignInDto signInDto) {
        UserEntity user = userRepository
                .findByLogin(signInDto.getLogin())
                .orElseThrow(() -> {
                    throw new UnauthorizedException("Некорректная почта и/или пароль.");
                });

        if (!passwordEncoder.matches(signInDto.getPassword(), user.getPassword())) {
            throw new UnauthorizedException("Некорректная почта и/или пароль.");
        }

        sendSuccessLoginMessage(user.getId());
        return new FullUserDto(
                userMapper.userToUserDto(user),
                jwtService.generateJwt(userMapper.userToJwtUserData(user))
        );
    }

    /**
     * Метод отправляет сообщение в RabbitMQ об успешной авторизации пользователя и создает уведомление для него.
     *
     * @param userId идентификатор пользователя
     */
    private void sendSuccessLoginMessage(UUID userId) {
        NewNotificationDto notification = NewNotificationDto
                .builder()
                .targetUserId(userId)
                .type(NotificationType.LOGIN_SUCCESS)
                .message("Вы успешно авторизовались в системе.")
                .build();

        streamBridge.send(CREATE_NOTIFICATION_OUT, notification);
    }
}
