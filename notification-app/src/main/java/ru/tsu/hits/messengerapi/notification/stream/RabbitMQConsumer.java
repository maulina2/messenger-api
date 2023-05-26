package ru.tsu.hits.messengerapi.notification.stream;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.tsu.hits.messengerapi.common.dto.NewNotificationDto;
import ru.tsu.hits.messengerapi.notification.service.NotificationService;

import java.util.function.Consumer;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class RabbitMQConsumer {

    private final NotificationService notificationService;

    @Bean
    public Consumer<NewNotificationDto> createNotification() {
        return notificationService::createNotification;
    }

}
