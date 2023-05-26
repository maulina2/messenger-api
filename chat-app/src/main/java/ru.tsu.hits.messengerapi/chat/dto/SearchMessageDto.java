package ru.tsu.hits.messengerapi.chat.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

/**
 * Дто, в которой передается поисковая строка для поиска сообщения в чате.
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class SearchMessageDto {

    @NotNull
    private String searchString;
}
