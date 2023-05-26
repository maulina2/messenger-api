package ru.tsu.hits.messengerapi.common.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Дто в которой передаются данные для синхронизации данных.
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class FullNameDto {
    private String fullName;

}
