package ru.tsu.hits.messengerapi.commonsecurity.filter;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import ru.tsu.hits.messengerapi.commonsecurity.dto.JwtUserData;
import ru.tsu.hits.messengerapi.commonsecurity.security.JwtAuthentication;
import ru.tsu.hits.messengerapi.commonsecurity.service.JwtService;
import ru.tsu.hits.messengerapi.commonsecurity.util.SecurityConstants;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Фильтр проверки jwt токена в пользовательских запросах.
 */
@Slf4j
@RequiredArgsConstructor
public class JwtTokenFilter extends OncePerRequestFilter {

    private final JwtService jwtService;

    /**
     * Метод, выполняющий фильтрацию запросов для проверки токенов в интеграционных клиентах.
     * Если токен отсутствует, возвращает 401 ошибку.
     * Если токен не проходит верификацию, возвращает 401 ошибку.
     * Если токен проходит верификацию, передает управление дальше по цепочке фильтров.
     *
     * @param request     объект типа HttpServletRequest, содержащий информацию о запросе.
     * @param response    объект типа HttpServletResponse, содержащий информацию о ответе.
     * @param filterChain объект типа FilterChain, представляющий цепочку фильтров, которые должны быть выполнены.
     * @throws ServletException если возникла ошибка при обработке запроса.
     * @throws IOException      если возникла ошибка ввода-вывода при обработке запроса.
     */
    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        log.info("Фильтр для токенов вступил в работу");
        String token = request.getHeader(SecurityConstants.HEADER_JWT_TOKEN);

        if (token == null) {
            log.info("Токен отсутствует, кидаю 401");
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            return;
        }

        if (token.startsWith("Bearer ")) {
            try {
                JwtUserData jwtUserData = jwtService.decode(token.substring(7));

                Authentication authentication = new JwtAuthentication(jwtUserData);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            } catch (Exception exception) {
                log.info("Токен не прошел верификацию, кидаю 401", exception);
                response.setStatus(HttpStatus.UNAUTHORIZED.value());
                return;
            }
        }

        log.info("С токеном все нормально");
        filterChain.doFilter(request, response);
    }
}
