package ru.tsu.hits.messengerapi.friends.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.tsu.hits.messengerapi.common.dto.IsAFriendDto;
import ru.tsu.hits.messengerapi.common.dto.UserDto;
import ru.tsu.hits.messengerapi.friends.entity.FriendEntity;
import ru.tsu.hits.messengerapi.friends.repository.FriendRepository;

import java.util.List;
import java.util.UUID;

/**
 * Сервис для обработки интеграционных запросов в друзьях.
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class FriendIntegrationRequestService {

    private final FriendRepository friendRepository;
    private final IntegrationRequestService integrationRequestService;

    /**
     * Проверяет, является ли пользователь с идентификатором externalUserId другом пользователя с идентификатором targetUserId.
     *
     * @param isAFriendDto объект, содержащий идентификаторы пользователей
     * @return true, если пользователи являются друзьями, false в противном случае
     */
    public boolean isAFriend(IsAFriendDto isAFriendDto) {
        integrationRequestService.checkId(isAFriendDto.getExternalUserId());
        return friendRepository.existsByTargetUserAndExternalUser(isAFriendDto.getTargetUserId(), isAFriendDto.getExternalUserId());
    }

    /**
     * Метод для обновления сущностей друзей пользователя в базе данных.
     *
     * @param id - id пользователя, по которому можно получить его полное имя из сервиса юзеров.
     */
    public void dataSynchronization(UUID id) {
        List<FriendEntity> friendEntities = friendRepository.findByExternalUser(id);
        for (FriendEntity friend : friendEntities) {
            friend.setFullName(integrationRequestService.getFullName(id).getFullName());
            friendRepository.save(friend);
        }
    }

    /**
     * Метод для синхронизации данных пользователя с его заблокированными контактами.
     * Обновляет полное имя пользователя в записях заблокированных контактов.
     *
     * @param userDto объект класса UserDto, содержащий данные пользователя
     */
    public void dataSynchronization(UserDto userDto) {
        List<FriendEntity> friendEntities = friendRepository.findByExternalUser(userDto.getId());
        friendEntities.forEach(friendEntity -> friendEntity.setFullName(userDto.getFullName()));
        friendRepository.saveAll(friendEntities);
    }
}
