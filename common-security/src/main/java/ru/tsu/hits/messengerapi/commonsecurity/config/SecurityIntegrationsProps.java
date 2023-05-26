package ru.tsu.hits.messengerapi.commonsecurity.config;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.stereotype.Service;

/**
 * Настройки security для интеграционного взаимодействия
 */
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Service
public class SecurityIntegrationsProps {

    private String apiKey;

    private String rootPath;

}
