package ru.tsu.hits.messengerapi.commonsecurity.service;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import ru.tsu.hits.messengerapi.commonsecurity.config.SecurityProps;
import ru.tsu.hits.messengerapi.commonsecurity.dto.JwtUserData;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.UUID;

import static java.lang.System.currentTimeMillis;

/**
 * Сервис для работы с JWT-токенами. Предоставляет методы для генерации и декодирования токенов.
 */
@Service
@RequiredArgsConstructor
public class JwtService {

    private final SecurityProps securityProps;

    /**
     * Генерирует JWT-токен на основе переданных данных пользователя.
     *
     * @param userData данные пользователя, на основе которых генерируется токен.
     * @return строковое представление сгенерированного токена.
     */
    public String generateJwt(JwtUserData userData) {
        Key key = Keys.hmacShaKeyFor(securityProps.getJwtToken().getSecret().getBytes(StandardCharsets.UTF_8));
        Date issAt = new Date();
        Date expAt = new Date(currentTimeMillis() + securityProps.getJwtToken().getExpiration());

        return Jwts.builder()
                .setSubject(userData.getLogin())
                .claim("id", userData.getId().toString())
                .claim("email", userData.getEmail())
                .claim("fullName", userData.getFullName())
                .setIssuedAt(issAt)
                .setExpiration(expAt)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * Декодирует JWT-токен и возвращает данные пользователя, закодированные в токене.
     *
     * @param token строковое представление JWT-токена.
     * @return объект JwtUserData, содержащий данные пользователя, закодированные в токене.
     */
    public JwtUserData decode(String token) {
        var key = Keys.hmacShaKeyFor(securityProps.getJwtToken().getSecret().getBytes(StandardCharsets.UTF_8));

        Jws<Claims> data = Jwts
                .parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token);

        Claims claims = data.getBody();

        return new JwtUserData(
                UUID.fromString(claims.get("id", String.class)),
                claims.getSubject(),
                claims.get("fullName", String.class),
                claims.get("email", String.class)
        );
    }

    /**
     * Метод для извлечения информации о пользователя в виде {@link JwtUserData}
     * из {@link Authentication} текущего запроса.
     *
     * @param authentication информация о пользователя для текущего HTTP запроса.
     * @return информация о пользователе в {@link JwtUserData}
     */
    public static JwtUserData extractPersonData(Authentication authentication) {
        return (JwtUserData) authentication.getPrincipal();
    }

    /**
     * Метод для извлечения идентификатора из {@link Authentication} текущего запроса.
     *
     * @param authentication информация о пользователя для текущего HTTP запроса.
     * @return идентификатор пользователя.
     */
    public static UUID extractId(Authentication authentication) {
        return ((JwtUserData) authentication.getPrincipal()).getId();
    }

}