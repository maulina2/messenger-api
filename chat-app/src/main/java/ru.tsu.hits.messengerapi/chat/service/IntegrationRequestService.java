package ru.tsu.hits.messengerapi.chat.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import ru.tsu.hits.messengerapi.common.dto.FileDto;
import ru.tsu.hits.messengerapi.common.dto.FullNameDto;
import ru.tsu.hits.messengerapi.common.dto.IsAFriendDto;
import ru.tsu.hits.messengerapi.common.dto.IsBlockedUserDto;
import ru.tsu.hits.messengerapi.common.exception.ForbiddenException;
import ru.tsu.hits.messengerapi.common.exception.InternalException;
import ru.tsu.hits.messengerapi.common.exception.NotFoundException;
import ru.tsu.hits.messengerapi.common.integration.IntegrationRequestSender;

import java.util.UUID;

/**
 * Сервис, который занимается обработкой данных, полученных от других сервисов, при отправке интеграционных запросов.
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class IntegrationRequestService {

    private final IntegrationRequestSender integrationRequestSender;

    /**
     * Проверяет, заблокирован ли целевой пользователь внешним пользователем
     * @param externalUserId идентификатор внешнего пользователя
     * @param targetUserId идентификатор целевого пользователя
     * @throws ForbiddenException если целевой пользователь заблокирован внешним пользователем
     * @throws NotFoundException если пользователь с идентификатором externalUserId не найден
     * @throws InternalException если произошло исключение во время выполнения интеграционного запроса
     */
    public void isBlockedUser(UUID externalUserId, UUID targetUserId) {
        try {
            IsBlockedUserDto requestBody = new IsBlockedUserDto(externalUserId, targetUserId);
            if (integrationRequestSender.friendsServicePersonIsBlocked(requestBody)) {
                throw new ForbiddenException("Пользователь c таким id " + externalUserId + " добавил вас в черный список");
            }
        }
        catch (HttpClientErrorException.NotFound exception) {
            throw new NotFoundException("Пользователь с таким id " + externalUserId + " не найден");
        }
        catch (ForbiddenException forbiddenException) {
            throw new ForbiddenException(forbiddenException.getMessage());
        }
        catch (Exception exception) {
            throw new InternalException("Исключение во время выполнения интеграционного запроса", exception);
        }
    }

    /**
     * Проверяет, является ли пользователь с идентификатором targetUserId другом пользователя с идентификатором externalUserId.
     * @param externalUserId идентификатор пользователя, который запрашивает информацию о дружбе
     * @param targetUserId идентификатор пользователя, о котором запрашивается информация
     * @throws ForbiddenException если пользователь с идентификатором externalUserId не является другом пользователя с идентификатором targetUserId
     * @throws NotFoundException если пользователь с идентификатором externalUserId или targetUserId не найден
     * @throws InternalException если возникло исключение во время выполнения интеграционного запроса
     */
    public void isAFriend(UUID externalUserId, UUID targetUserId) {
        try {
            IsAFriendDto requestBody = new IsAFriendDto(externalUserId, targetUserId);
            if (!integrationRequestSender.friendsServicePersonIsFriend(requestBody)) {
                throw new ForbiddenException("Пользователь c таким id " + externalUserId + " отсутствует у вас в друзьях");
            }
        }
        catch (HttpClientErrorException.NotFound exception) {
            throw new NotFoundException("Пользователь с таким id " + externalUserId + " не найден");
        }
        catch (ForbiddenException forbiddenException) {
            throw new ForbiddenException("Пользователь c таким id " + externalUserId + " отсутствует у вас в друзьях");
        }
        catch (Exception exception){
            throw new InternalException("Ошибка во время выполнения интеграционного запроса.", exception);
        }
    }

    /**
     * Метод для проверки на существование пользователя, котрого хотят добавить в друзья.
     *
     * @param id id пользователя
     */
    public FullNameDto getFullName(UUID id) {
        try {
            return integrationRequestSender.usersServiceGetFullName(id);
        }
        catch (HttpClientErrorException.NotFound exception) {
            throw new NotFoundException("Пользователь с таким id " + id + " не найден");
        }
        catch (Exception exception){
            throw new InternalException("Ошибка во время выполнения интеграционного запроса.", exception);
        }
    }

    /**
     * Получает данные файла в виде объекта FileDto по его идентификатору.
     *
     * @param id идентификатор файла
     * @return объект FileDto с данными файла
     * @throws NotFoundException если файл с указанным идентификатором не найден
     * @throws InternalException если произошла ошибка во время выполнения интеграционного запроса
     */
    public FileDto getFileData(UUID id){
        try {
            return integrationRequestSender.fileServiceGetFileData(id);
        }
        catch (HttpClientErrorException.NotFound exception) {
            throw new NotFoundException("Файл с таким id " + id + " не найден");
        }
        catch (Exception exception){
            throw new InternalException("Ошибка во время выполнения интеграционного запроса.", exception);
        }
    }

}
