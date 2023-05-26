package ru.tsu.hits.messengerapi.filestorage;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import ru.tsu.hits.messengerapi.commonsecurity.ImportSecurityAnnotation;

@SpringBootApplication
@ImportSecurityAnnotation
@ConfigurationPropertiesScan("ru.tsu.hits.messengerapi.filestorage.config")
public class FileStorageServiceApplication {

    /**
     * Главный метод модуля. Он собирает весь модуль.
     */
    public static void main(String[] args) {
        SpringApplication.run(FileStorageServiceApplication.class, args);
    }

}
