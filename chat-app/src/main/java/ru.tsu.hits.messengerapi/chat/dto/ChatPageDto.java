package ru.tsu.hits.messengerapi.chat.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Дто, в которой возвращается страница с чатами.
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class ChatPageDto {

    private PageDto pageDto;

    private List<ExtendedChatDto> chatDtos;
}
