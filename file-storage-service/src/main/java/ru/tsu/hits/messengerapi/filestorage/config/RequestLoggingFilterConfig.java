package ru.tsu.hits.messengerapi.filestorage.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.AbstractRequestLoggingFilter;
import org.springframework.web.filter.ServletContextRequestLoggingFilter;


/**
 * Класс для конфигурации логгера.
 */
@Configuration
public class RequestLoggingFilterConfig {

    /**
     * Метод в котором задаются настройки логгера.
     */
    @Bean
    public AbstractRequestLoggingFilter logFilter() {
        ServletContextRequestLoggingFilter filter = new ServletContextRequestLoggingFilter();
        filter.setIncludeClientInfo(true);
        filter.setIncludeQueryString(true);
        filter.setIncludePayload(true);
        filter.setIncludeHeaders(true);
        filter.setAfterMessagePrefix("REQUEST DATA: ");
        return filter;
    }
}