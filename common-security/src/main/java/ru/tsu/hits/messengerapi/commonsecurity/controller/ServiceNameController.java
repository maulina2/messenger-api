package ru.tsu.hits.messengerapi.commonsecurity.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.tsu.hits.messengerapi.commonsecurity.config.SecurityProps;

/**
 * Контроллер для проверки доступности сервиса и его состояния.
 */
@RequiredArgsConstructor
@RestController
public class ServiceNameController {

    private final SecurityProps securityProps;
    @Value("${app.name}")
    private String appName;

    /**
     * Возвращает сообщение о доступности сервиса с указанием его имени.
     *   @return строку с сообщением о доступности сервиса с указанием его имени.
     */
    @GetMapping("/health")
    public String health() {
        return "Сервис " + appName + " доступен";
    }


}
