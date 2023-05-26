package ru.tsu.hits.messengerapi.common.exception;


/**
 * Класс кастомной ошибки.
 * BadRequestException используется при некорректных входных данных в запросе
 */
public class BadRequestException extends IllegalArgumentException {

    public BadRequestException(String message) {
        super(message);
    }

}
