package ru.tsu.hits.messengerapi.common.exception;

/**
 * Класс кастомной ошибки.
 * ForbiddenException используется, когда у пользователя нет прав на получение какой либо информации.
 */
public class ForbiddenException extends RuntimeException {

    public ForbiddenException(String message) {
        super(message);
    }
}
