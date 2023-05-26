package ru.tsu.hits.messengerapi.chat.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Дто, в которой передается информация о найденных сообщениях.
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class FoundMessageDto {

    private UUID chatId;

    private String name;

    @JsonFormat(pattern = "yyyy-MM-dd hh:mm")
    private LocalDateTime messageSendDate;

    private String text;

    private boolean isHaveAttachment;

    private String attachmentName;

    private UUID authorId;
}
