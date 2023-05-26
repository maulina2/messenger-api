package ru.tsu.hits.messengerapi.commonsecurity.config;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.RequestMatcher;
import ru.tsu.hits.messengerapi.commonsecurity.filter.IntegrationFilter;
import ru.tsu.hits.messengerapi.commonsecurity.filter.JwtTokenFilter;
import ru.tsu.hits.messengerapi.commonsecurity.service.JwtService;

import java.util.Arrays;
import java.util.Objects;

@Slf4j
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true, jsr250Enabled = true)
@RequiredArgsConstructor
public class SecurityConfig {

    private final SecurityProps securityProps;
    private final JwtService jwtService;

    /**
     * Шифровальщик паролей
     */
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Настройка для JWT
     */
    @SneakyThrows
    @Bean
    public SecurityFilterChain filterChainJwt(HttpSecurity http) {
        http = http.addFilterBefore(
                        new JwtTokenFilter(jwtService),
                        UsernamePasswordAuthenticationFilter.class
                )
                .requestMatcher(filterPredicate(
                        securityProps.getJwtToken().getRootPath(),
                        securityProps.getJwtToken().getPermitAll()
                ))
        ;
        return finalize(http);
    }

    /**
     * Настройка для интеграции между сервисами
     */
    @SneakyThrows
    @Bean
    public SecurityFilterChain filterChainIntegration(HttpSecurity http) {
        http = http.requestMatcher(filterPredicate(securityProps.getIntegration().getRootPath()))
                .addFilterBefore(
                        new IntegrationFilter(securityProps.getIntegration().getApiKey()),
                        UsernamePasswordAuthenticationFilter.class
                );
        return finalize(http);
    }

    /**
     * Создает цепочку фильтров безопасности для HttpSecurity с отключенной защитой от CSRF и без сохранения состояния сессии.
     *
     * @param http объект HttpSecurity, для которого создается цепочка фильтров безопасности.
     * @return объект SecurityFilterChain, представляющий собой цепочку фильтров безопасности.
     */
    @SneakyThrows
    private SecurityFilterChain finalize(HttpSecurity http) {
        return http.csrf()
                .disable()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .build();
    }

    /**
     * Создает объект RequestMatcher, который определяет, должен ли запрос быть обработан фильтром безопасности.
     *
     *   @param rootPath корневой путь, который должен содержаться в пути запроса.
     *   @param ignore список путей, которые не должны обрабатываться фильтром безопасности.
     *   @return объект RequestMatcher, который определяет, должен ли запрос быть обработан фильтром безопасности.
     */
    private RequestMatcher filterPredicate(String rootPath, String... ignore) {
        return req -> Objects.nonNull(req.getServletPath())
                && req.getServletPath().startsWith(rootPath)
                && Arrays.stream(ignore).noneMatch(item -> req.getServletPath().startsWith(item));
    }

}
