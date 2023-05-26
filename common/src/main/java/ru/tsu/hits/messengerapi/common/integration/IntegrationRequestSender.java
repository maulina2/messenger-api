package ru.tsu.hits.messengerapi.common.integration;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ru.tsu.hits.messengerapi.common.dto.FileDto;
import ru.tsu.hits.messengerapi.common.dto.FullNameDto;
import ru.tsu.hits.messengerapi.common.dto.IsAFriendDto;
import ru.tsu.hits.messengerapi.common.dto.IsBlockedUserDto;
import ru.tsu.hits.messengerapi.common.util.Constants;

import java.util.List;
import java.util.UUID;

/**
 * Сервис для отправки интеграционных запросов.
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class IntegrationRequestSender {

    @Value("${app.security.integration.api-key}")
    private String apiKey;

    /**
     * Метод отправляет GET-запрос на сервер, чтобы получить полное имя пользователя по его идентификатору.
     * @param body идентификатор пользователя в формате UUID
     * @return возвращает тело ответа на запрос в виде объекта типа Object, содержащего полное имя пользователя
     */
    public FullNameDto usersServiceGetFullName(UUID body) {
        HttpHeaders headers = buildHeaders();
        return new RestTemplate().exchange(
                Constants.GET_FULL_NAME_REQUEST + body,
                HttpMethod.GET,
                new HttpEntity<>(headers),
                FullNameDto.class).getBody();
    }

    public FileDto fileServiceGetFileData(UUID fileId) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = buildHeaders();
        HttpEntity<Object> request = new HttpEntity<>(headers);
        return restTemplate.exchange(
                Constants.GET_FILE_DATA_REQUEST + fileId,
                HttpMethod.GET,
                request,
                FileDto.class
        ).getBody();
    }

    /**
     * Метод для отправки POST-запроса на проверку заблокирован ли пользователь.
     *
     * @param body DTO с информацией о пользователе, для которого нужна проверка.
     * @return ResponseEntity с ответом на запрос.
     * @throws RuntimeException если произошла ошибка во время формирования URI для запроса или при выполнении запроса.
     */
    public boolean friendsServicePersonIsBlocked(IsBlockedUserDto body) {
        HttpHeaders headers = buildHeaders();
        HttpEntity<IsBlockedUserDto> entityBody = new HttpEntity<>(body, headers);
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.postForEntity(Constants.IS_BLOCKED_REQUEST, entityBody, boolean.class).getBody();
    }

    /**
     * Метод отправляет POST-запрос на сервер, чтобы проверить, является ли человек другом.
     * @param body объект типа IsAFriendDto, содержащий информацию о пользователе и его друге
     * @return возвращает тело ответа на запрос в виде объекта типа Object
     */
    public boolean friendsServicePersonIsFriend(IsAFriendDto body) {
        HttpHeaders headers = buildHeaders();
        HttpEntity<IsAFriendDto> entityBody = new HttpEntity<>(body, headers);
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.postForEntity(Constants.IS_FRIEND_REQUEST, entityBody, boolean.class).getBody();
    }

    /**
     * Метод для формирования заголовков запроса.
     *
     * @return HttpHeaders с заданными параметрами.
     */
    private HttpHeaders buildHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        headers.set(Constants.API_KEY, apiKey);
        return headers;
    }
}
