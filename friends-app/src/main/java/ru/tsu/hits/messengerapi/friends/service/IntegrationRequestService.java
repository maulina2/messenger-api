package ru.tsu.hits.messengerapi.friends.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import ru.tsu.hits.messengerapi.common.dto.FullNameDto;
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

    private final ObjectMapper objectMapper;


    /**
     * Метод для проверки на существование пользователя, которого хотят добавить в друзья.
     *
     * @param id id пользователя
     */
    public FullNameDto checkId(UUID id) {
        try {
          return integrationRequestSender.usersServiceGetFullName(id);
        } catch (HttpClientErrorException.NotFound exception) {
            throw new NotFoundException("Пользователь с таким id " + id + " не найден");
        } catch (Exception exception) {
            throw new InternalException("Ошибка во время выполнения интеграционного запроса.");
        }
    }

    /**
     * Метод для проверки на существование пользователя, котрого хотят добавить в друзья.
     *
     * @param id id пользователя
     */
    public FullNameDto getFullName(UUID id) {
        try {
            var responseBody = integrationRequestSender.usersServiceGetFullName(id);
            return objectMapper.convertValue(responseBody, FullNameDto.class);
        }
        catch (HttpClientErrorException.NotFound exception) {
            throw new NotFoundException("Пользователь с таким id " + id + " не найден");
        }
        catch (Exception exception){
            throw new InternalException("Ошибка во время выполнения интеграционного запроса.");
        }
    }
}
