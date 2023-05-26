package ru.tsu.hits.messengerapi.friends.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;

/**
 * Дто, в которой получается информация о пагинации с фильтрацией для черного списка текущего пользователя.
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class BlackListPageDto {
    @Valid
    private PageableDto pageableDto;
    private String fullNameFilter;
}
