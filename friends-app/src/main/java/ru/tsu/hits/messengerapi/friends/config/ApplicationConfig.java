package ru.tsu.hits.messengerapi.friends.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.tsu.hits.messengerapi.common.config.SwaggerConfig;
import ru.tsu.hits.messengerapi.common.integration.IntegrationRequestSender;
import ru.tsu.hits.messengerapi.common.service.ConvertDateService;

@Configuration
public class ApplicationConfig {

    /**
     * Создает бин с классом для отправки интеграционных запросов
     *
     * @return бин {@link IntegrationRequestSender}.
     */
    @Bean
    public IntegrationRequestSender commonIntegrationRequestSender() {
        return new IntegrationRequestSender();
    }

    /**
     * Создает бин с конфигом для сваггера.
     *
     * @return бин {@link SwaggerConfig}.
     */
    @Bean
    SwaggerConfig swaggerConfig() {
        return new SwaggerConfig();
    }

    /**
     * Создает бин с классом для конвертации даты
     *
     * @return бин {@link ConvertDateService}.
     */
    @Bean
    public ConvertDateService convertDateService(){
        return new ConvertDateService();
    }

}
