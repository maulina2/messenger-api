package ru.tsu.hits.messengerapi.chat;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import ru.tsu.hits.messengerapi.commonsecurity.ImportSecurityAnnotation;


@SpringBootApplication
@ImportSecurityAnnotation
public class ChatApiApplication {
    /**
     * Главный метод модуля friends-app. Он собирает весь модуль.
     */
    public static void main(String[] args) {
        SpringApplication.run(ChatApiApplication.class, args);
    }

}
