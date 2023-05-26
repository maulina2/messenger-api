package ru.tsu.hits.messengerapi.notification;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import ru.tsu.hits.messengerapi.commonsecurity.ImportSecurityAnnotation;

@SpringBootApplication
@ImportSecurityAnnotation
public class NotificationAppApplication {

    public static void main(String[] args) {
        SpringApplication.run(NotificationAppApplication.class, args);
    }

}
