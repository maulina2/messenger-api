package ru.tsu.hits.messengerapi.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import ru.tsu.hits.messengerapi.dto.ApiError;
import ru.tsu.hits.messengerapi.exception.NotFoundException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Контроллер для обработки исключений. Здесь должны отлавливаться и обрабатываться все ошибки,
 * которые идут на контроллер.
 */
@ControllerAdvice
@Slf4j
public class ExceptionHandlerController extends ResponseEntityExceptionHandler {

    /**
     * Метод для обработки исключений для невалидных тел запросов.
     *
     * @param exception исключение.
     * @param headers   заголовки, которые будут записаны в ответ.
     * @param status    выбранный статус ответа.
     * @param request   текущий запрос.
     * @return {@link Map}, где ключ - название поля невалидного тела запроса,
     * а значение - список {@code user-friendly} сообщений об ошибке.
     */
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException exception,
            HttpHeaders headers,
            HttpStatus status,
            WebRequest request
    ) {

        Map<String, List<String>> errors = new HashMap<>();

        exception
                .getBindingResult()
                .getAllErrors()
                .forEach(error -> {
                    String fieldName = ((FieldError) error).getField();
                    String message = error.getDefaultMessage();

                    if (message != null) {
                        if (errors.containsKey(fieldName)) {
                            errors.get(fieldName).add(message);
                        } else {
                            List<String> newErrorList = new ArrayList<>();
                            newErrorList.add(message);

                            errors.put(fieldName, newErrorList);
                        }
                    }
                });

        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    /**
     * Метод для отлавливания всех {@link NotFoundException}.
     *
     * @param exception исключение.
     * @return объект класса {@link ApiError} со статус кодом 404.
     */
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ApiError> handleNotFoundException(NotFoundException exception) {
        return new ResponseEntity<>(new ApiError(exception.getMessage()), HttpStatus.NOT_FOUND);
    }


}