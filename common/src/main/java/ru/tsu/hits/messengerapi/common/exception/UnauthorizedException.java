package ru.tsu.hits.messengerapi.common.exception;


/**
 * Класс кастомной ошибки.
 * UnauthorizedException используется при ситуации, когда пользователя не смогли аутентифицировать.
 */
public class UnauthorizedException extends RuntimeException {

    public UnauthorizedException(String message) {
        super(message);
    }

}
