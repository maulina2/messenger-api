package ru.tsu.hits.messengerapi.chat.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.tsu.hits.messengerapi.chat.enumeration.ChatType;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Дто, в которой передается информация о чате для пагинации.
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class ExtendedChatDto {

    private UUID id;

    private String name;

    private ChatType chatType;

    @JsonFormat(pattern = "yyyy-MM-dd hh:mm")
    private LocalDateTime lastMessageSendDate;

    private String lastMessage;

    private boolean isHaveAttachment;

    private UUID lastMessageAuthorId;
}
