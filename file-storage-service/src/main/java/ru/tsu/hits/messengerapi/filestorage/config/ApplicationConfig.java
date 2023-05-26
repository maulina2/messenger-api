package ru.tsu.hits.messengerapi.filestorage.config;


import com.ibm.icu.text.Transliterator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.tsu.hits.messengerapi.common.config.SwaggerConfig;

@Configuration
public class ApplicationConfig {

    private static final String CYRILLIC_TO_LATIN = "Russian-Latin/BGN";

    /**
     * Создает бин с конфигом для сваггера.
     *
     * @return бин {@link SwaggerConfig}.
     */
    @Bean
    SwaggerConfig swaggerConfig() {
        return new SwaggerConfig();
    }

    @Bean
    Transliterator cyrillicTransliterator() {
        return Transliterator.getInstance(CYRILLIC_TO_LATIN);
    }
}
