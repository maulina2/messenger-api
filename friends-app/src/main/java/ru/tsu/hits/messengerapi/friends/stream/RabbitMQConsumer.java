package ru.tsu.hits.messengerapi.friends.stream;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.tsu.hits.messengerapi.common.dto.UserDto;
import ru.tsu.hits.messengerapi.friends.service.BlackListIntegrationRequestService;
import ru.tsu.hits.messengerapi.friends.service.FriendIntegrationRequestService;

import java.util.function.Consumer;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class RabbitMQConsumer {

    private final BlackListIntegrationRequestService blackListIntegrationRequestService;
    private final FriendIntegrationRequestService friendIntegrationRequestService;

    /**
     * Метод возвращает объект Consumer, который принимает объект класса UserDto и синхронизирует данные пользователя.
     * Обновляет полное имя пользователя в записях заблокированных контактов.
     *
     */
    @Bean
    public Consumer<UserDto> synchronizeUserInfo() {
        return userDto -> {
            log.info("Сообщение из RabbitMQ {}'", userDto.getFullName());
            blackListIntegrationRequestService.dataSynchronization(userDto);
            friendIntegrationRequestService.dataSynchronization(userDto);
        };
    }

}
