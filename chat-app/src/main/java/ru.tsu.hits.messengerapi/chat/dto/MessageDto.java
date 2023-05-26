package ru.tsu.hits.messengerapi.chat.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * Дто, в которой получается информация о сообщении в чате
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class MessageDto {

    private UUID id;
    @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss")
    private LocalDateTime sendDate;

    private String text;
    private UUID authorId;

    private List<AttachmentDto> attachmentDtos;
}
