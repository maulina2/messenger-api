package ru.tsu.hits.messengerapi.chat.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.tsu.hits.messengerapi.common.dto.PageableDto;

import javax.validation.Valid;

/**
 * Дто, в которой получается информация о пагинации с фильтрацией для списка чатов текущего пользователя.
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class PageDto {

    @Valid
    private PageableDto pageableDto;
    private String chatNameFilter;

}
