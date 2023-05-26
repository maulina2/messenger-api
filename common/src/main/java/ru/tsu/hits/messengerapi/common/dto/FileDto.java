package ru.tsu.hits.messengerapi.common.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Дто в которой передаются данные о файле.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FileDto {

    private UUID fileId;

    private String name;

    private UUID authorId;

    private LocalDateTime uploadDateTime;
}
