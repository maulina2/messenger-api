package ru.tsu.hits.messengerapi.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.tsu.hits.messengerapi.common.dto.UserDto;
import ru.tsu.hits.messengerapi.common.util.RabbitMQBindings;
import ru.tsu.hits.messengerapi.user.dto.EditUserInfoDto;
import ru.tsu.hits.messengerapi.user.entity.UserEntity;
import ru.tsu.hits.messengerapi.user.mapper.UserMapper;
import ru.tsu.hits.messengerapi.user.repository.UserRepository;

import java.util.Objects;
import java.util.UUID;

/**
 * Сервис для получения и редактирования информации о себе.
 */
@Service
@RequiredArgsConstructor
public class AccountService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final FindUserService findUserService;
    private final StreamBridge streamBridge;
    private final IntegrationRequestService integrationRequestService;

    /**
     * Метод для получения информации о пользователе по его идентификатору.
     *
     * @param id идентификатор пользователя.
     * @return объект типа UserDto, содержащий информацию о пользователе.
     */
    @Transactional(readOnly = true)
    public UserDto getUserInfo(UUID id) {
        UserEntity user = findUserService.getUserById(id);
        return userMapper.userToUserDto(user);
    }

    /**
     * Метод для редактирования информации о пользователе по его идентификатору.
     *
     * @param id              идентификатор пользователя.
     * @param editUserInfoDto объект типа EditUserInfoDto, содержащий новые данные пользователя.
     * @return объект типа UserDto, содержащий обновленную информацию о пользователе.
     */
    @Transactional
    public UserDto editUserInfo(UUID id, EditUserInfoDto editUserInfoDto) {
        UserEntity user = findUserService.getUserById(id);
        String oldFullName = user.getFullName();

        if(editUserInfoDto.getAvatar() != null){
            integrationRequestService.isValidFileId(editUserInfoDto.getAvatar());
        }
        user.setBirthDate(editUserInfoDto.getBirthDate());
        user.setFullName(editUserInfoDto.getFullName());
        user.setAvatar(editUserInfoDto.getAvatar());
        user.setPhoneNumber(editUserInfoDto.getPhoneNumber());
        user.setCity(editUserInfoDto.getCity());
        user = userRepository.save(user);

        UserDto userDto = userMapper.userToUserDto(user);
        if (!Objects.equals(oldFullName, editUserInfoDto.getFullName())){
            synchronizeUserInfo(userDto);
        }

        return userDto;
    }

    /**
     * Метод отправляет сообщение в RabbitMQ о необходимости синхронизировать информацию
     * о пользователе.
     *
     * @param userDto объект UserDto, содержащий информацию о пользователе
     */
    private void synchronizeUserInfo(UserDto userDto) {
        streamBridge.send(RabbitMQBindings.SYNCHRONIZE_USER_INFO_OUT, userDto);
    }
}
