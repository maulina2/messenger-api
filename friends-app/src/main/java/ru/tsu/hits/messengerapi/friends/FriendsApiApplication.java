package ru.tsu.hits.messengerapi.friends;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import ru.tsu.hits.messengerapi.commonsecurity.ImportSecurityAnnotation;

@SpringBootApplication
@ImportSecurityAnnotation
public class FriendsApiApplication {
    /**
     * Главный метод модуля friends-app. Он собирает весь модуль.
     */
    public static void main(String[] args) {
        SpringApplication.run(FriendsApiApplication.class, args);
    }

}
