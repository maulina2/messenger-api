package ru.tsu.hits.messengerapi.common.exception;

/**
 * Класс кастомной ошибки.
 * NotFoundException используется при ситуации, когда данные не были найдены.
 */
public class NotFoundException extends RuntimeException {

    public NotFoundException(String message) {
        super(message);
    }

}
