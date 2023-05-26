package ru.tsu.hits.messengerapi.chat.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.tsu.hits.messengerapi.chat.enumeration.ChatType;

import java.util.Date;
import java.util.UUID;


/**
 * Дто, в которой получается информация о чате.
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class ChatDto {

    private UUID id;

    private ChatType chatType;

    private Date chatCreationDate;

    private String name;

    private UUID admin;

    private UUID avatarId;

}
