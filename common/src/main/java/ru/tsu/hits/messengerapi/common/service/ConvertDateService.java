package ru.tsu.hits.messengerapi.common.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Date;

/**
 * Сервис ConvertDateService предназначен для преобразования объектов типа LocalDate в объекты типа Date.
 */
@Service
@RequiredArgsConstructor
public class ConvertDateService {

    /**
     * Метод convertToDate преобразует объект типа LocalDate в объект типа Date.
     *
     * @param dateToConvert объект LocalDate, который необходимо преобразовать
     * @return объект типа Date, полученный в результате преобразования
     */
    public Date convertToDate(LocalDate dateToConvert) {
        return java.sql.Date.valueOf(dateToConvert);
    }
}
