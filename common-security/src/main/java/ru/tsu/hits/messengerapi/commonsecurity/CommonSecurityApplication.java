package ru.tsu.hits.messengerapi.commonsecurity;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@ConfigurationPropertiesScan("ru.tsu.hits.messengerapi.commonsecurity")
@SpringBootApplication
public class CommonSecurityApplication {

    /**
     * Главный метод модуля common-security. Он собирает весь модуль.
     */
    public static void main(String[] args) {
        SpringApplication.run(CommonSecurityApplication.class, args);
    }

}
