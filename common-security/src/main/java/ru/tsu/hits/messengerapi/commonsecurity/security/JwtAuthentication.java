package ru.tsu.hits.messengerapi.commonsecurity.security;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import ru.tsu.hits.messengerapi.commonsecurity.dto.JwtUserData;

/**
 * Данные {@link org.springframework.security.core.Authentication} по запросам с UI.
 * В качестве principal и details - {@link JwtUserData}
 */
public class JwtAuthentication extends AbstractAuthenticationToken {

    /**
     * Конструктор класса.
     *
     * @param jwtUserData данные пользователя, полученные из JWT-токена.
     */
    public JwtAuthentication(JwtUserData jwtUserData) {
        super(null);
        this.setDetails(jwtUserData);
        setAuthenticated(true);
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    /**
     * Метод для получения информации о пользователе, полученной из JWT-токена.
     *
     * @return объект с данными пользователя.
     */
    @Override
    public Object getPrincipal() {
        return getDetails();
    }

}
