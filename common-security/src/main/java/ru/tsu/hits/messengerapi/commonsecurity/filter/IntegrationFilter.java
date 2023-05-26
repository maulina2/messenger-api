package ru.tsu.hits.messengerapi.commonsecurity.filter;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import ru.tsu.hits.messengerapi.commonsecurity.security.IntegrationAuthentication;
import ru.tsu.hits.messengerapi.commonsecurity.util.SecurityConstants;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Фильтр проверки ключа аутентификации API KEY в интеграционных запросах
 */
@Slf4j
@RequiredArgsConstructor
public class IntegrationFilter extends OncePerRequestFilter {

    private final String apiKey;

    /**
     * Метод, выполняющий фильтрацию запросов для интеграционных клиентов.
     *
     * @param request  объект типа HttpServletRequest, содержащий информацию о запросе.
     * @param response объект типа HttpServletResponse, содержащий информацию о ответе.
     * @param filterChain объект типа FilterChain, представляющий цепочку фильтров, которые должны быть выполнены.
     *
     * @throws ServletException если возникла ошибка при обработке запроса.
     * @throws IOException если возникла ошибка ввода-вывода при обработке запроса.
     */
    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        log.info("Фильтр для интеграционных запросов вступил в работу");
        String receivedApiKey = request.getHeader(SecurityConstants.HEADER_API_KEY);

        if (!apiKey.equals(receivedApiKey)) {
            log.info("Ключ для интеграционных не подошел, кидаю 401");
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            return;
        }

        log.info("Ключ для интеграционных подошел");
        SecurityContextHolder.getContext().setAuthentication(new IntegrationAuthentication());
        filterChain.doFilter(request, response);
    }
}
