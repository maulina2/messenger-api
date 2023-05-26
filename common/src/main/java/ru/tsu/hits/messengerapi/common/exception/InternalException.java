package ru.tsu.hits.messengerapi.common.exception;

/**
 * Класс кастомной ошибки.
 * InternalException используется, когда произошла внутренняя ошибка сервера.
 */
public class InternalException extends RuntimeException{
    public InternalException(String message) {
        super(message);
    }

    public InternalException(String message, Throwable cause) {
        super(message, cause);
    }
}
