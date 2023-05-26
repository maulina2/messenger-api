package ru.tsu.hits.messengerapi.common.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.context.annotation.Configuration;
import ru.tsu.hits.messengerapi.common.util.Constants;

/**
 * Конфигурационный класс для настройки Swagger UI.
 */
@Configuration
@OpenAPIDefinition(info = @Info(title = "messenger-api", version = "v1"))
@SecurityScheme(
        name = "bearerAuth",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        scheme = "Bearer"
)
@SecurityScheme(
        name = Constants.API_KEY,
        type = SecuritySchemeType.APIKEY,
        scheme = Constants.API_KEY,
        in = SecuritySchemeIn.HEADER
)
public class SwaggerConfig {

}
