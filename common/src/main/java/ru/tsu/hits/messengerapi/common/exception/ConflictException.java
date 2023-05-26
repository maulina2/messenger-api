package ru.tsu.hits.messengerapi.common.exception;

/**
 * Класс кастомной ошибки.
 * ConflictException используется при ситуации, когда запрос конфликтует с текущим состоянием сервера.
 */
public class ConflictException extends RuntimeException {

    public ConflictException(String message) {
        super(message);
    }

}
