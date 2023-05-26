package ru.tsu.hits.messengerapi.common.exception;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;


/**
 * Дто для формирования тела ответа в случае ошибки.
 */
@Data
public class ApiError {

    private List<String> messages;

    public ApiError(String message) {
        this.messages = new ArrayList<>();
        this.messages.add(message);
    }

}
