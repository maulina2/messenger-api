package ru.tsu.hits.messengerapi.user.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * Дто для перечисления полей, по которым можно отфильтровать список пользователей.
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class FiltersDto {
    private String fullNameFilter;
    private String emailFilter;
    private String loginFilter;
    private String cityFilter;
    private LocalDate birthDateFilter;
    private String phoneNumberFilter;
}
