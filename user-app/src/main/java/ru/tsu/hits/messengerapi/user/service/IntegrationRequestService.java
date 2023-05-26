package ru.tsu.hits.messengerapi.user.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import ru.tsu.hits.messengerapi.common.dto.IsBlockedUserDto;
import ru.tsu.hits.messengerapi.common.exception.ForbiddenException;
import ru.tsu.hits.messengerapi.common.exception.InternalException;
import ru.tsu.hits.messengerapi.common.exception.NotFoundException;
import ru.tsu.hits.messengerapi.common.integration.IntegrationRequestSender;

import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Service
public class IntegrationRequestService {

    private final IntegrationRequestSender integrationRequestSender;

    private final ObjectMapper objectMapper;


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
            var responseBody = integrationRequestSender.friendsServicePersonIsBlocked(requestBody);
            boolean convertedBody = objectMapper.convertValue(responseBody, boolean.class);
            if (convertedBody) {
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
            throw new InternalException("Исключение во время выполнения интеграционного запроса");
        }
    }


    /**
     * Метод проверяет валидность id файла
     *
     * @throws NotFoundException, если файл с указанным id не найден
     * @throws InternalException, если происходит любая другая ошибка во время выполнения интеграционного запроса
     * @param id идентификатор файла, который нужно проверить
     */
    public void isValidFileId(UUID id){
        try {
           integrationRequestSender.fileServiceGetFileData(id);
        }
        catch (HttpClientErrorException.NotFound exception) {
            throw new NotFoundException("Файл с таким id " + id + " не найден");
        }
        catch (Exception exception){
            throw new InternalException("Ошибка во время выполнения интеграционного запроса.", exception);
        }
    }

}
