package ru.tsu.hits.messengerapi.friends.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;

/**
 * Дто, в которой передается информация для поиска друзей текущего пользователя.
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class SearchFriendDto {
    @Valid
    private PageableDto pageableDto;

    private FiltersDto filters;
}
