package ru.tsu.hits.messengerapi.friends.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.tsu.hits.messengerapi.common.dto.UserDto;
import ru.tsu.hits.messengerapi.common.exception.InternalException;
import ru.tsu.hits.messengerapi.friends.entity.BlockedUserEntity;
import ru.tsu.hits.messengerapi.friends.repository.BlackListRepository;

import java.util.List;
import java.util.UUID;

/**
 * Сервис для обработки интеграционных запросов в черном списке.
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class BlackListIntegrationRequestService {
    private final BlackListRepository blackListRepository;

    private final IntegrationRequestService integrationRequestService;

    /**
     * Метод для синхронизации данных заблокированных пользователей.
     *
     * @param id идентификатор пользователя, чьи данные нужно синхронизировать с другими сервисами.
     * @throws InternalException если произошла внутренняя ошибка при выполнении запроса или ответ не содержит тела.
     */
    @Transactional
    public void dataSynchronization(UUID id) {
        List<BlockedUserEntity> blockedUserEntities = blackListRepository.findByExternalUser(id);
        for (BlockedUserEntity blockedUser : blockedUserEntities) {
            blockedUser.setFullName(integrationRequestService.getFullName(id).getFullName());
            blackListRepository.save(blockedUser);
        }
    }

    /**
     * Метод для синхронизации данных заблокированных пользователей.
     * Обновляет полное имя пользователя в записях заблокированных контактов.
     *
     * @param userDto объект класса UserDto, содержащий данные пользователя
     */
    @Transactional
    public void dataSynchronization(UserDto userDto) {
        List<BlockedUserEntity> blockedUserEntities = blackListRepository.findByExternalUser(userDto.getId());
        blockedUserEntities.forEach(blockedUserEntity -> blockedUserEntity.setFullName(userDto.getFullName()));
        blackListRepository.saveAll(blockedUserEntities);
    }

}
