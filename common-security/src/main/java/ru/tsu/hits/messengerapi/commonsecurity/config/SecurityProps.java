package ru.tsu.hits.messengerapi.commonsecurity.config;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Java-модель конфига по пути app.security в application.yml
 */
@ConfigurationProperties("app.security")
@Getter
@Setter
@ToString
public class SecurityProps {

    private SecurityJwtTokenProps jwtToken;

    private SecurityIntegrationsProps integration;

}
