package ru.tsu.hits.messengerapi.chat.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

/**
 * Дто, в которой получается информация о вложении в сообщении
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class AttachmentDto {

    private String name;

    private UUID fileId;

}
