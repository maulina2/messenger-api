package ru.tsu.hits.messengerapi.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.tsu.hits.messengerapi.common.dto.FullNameDto;
import ru.tsu.hits.messengerapi.user.entity.UserEntity;
import ru.tsu.hits.messengerapi.user.mapper.UserMapper;

import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserIntegrationRequestService {

    private final FindUserService findUserService;
    private final UserMapper userMapper;

    /**
     * Метод для получения полного имени пользователя по его идентификатору.
     *
     * @param id идентификатор пользователя
     * @return объект класса FullNameDto, содержащий полное имя пользователя
     */
    public FullNameDto getUserFullName(UUID id) {
        UserEntity user = findUserService.getUserById(id);
        return userMapper.userToFullNameDto(user);
    }
}
