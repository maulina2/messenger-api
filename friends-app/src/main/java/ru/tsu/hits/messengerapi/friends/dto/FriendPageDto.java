package ru.tsu.hits.messengerapi.friends.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;

/**
 * Дто, в которой получается информация о пагинации с фильтрацией для списка друзей текущего пользователя.
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class FriendPageDto {

   @Valid
    private PageableDto pageableDto;
    private String fullNameFilter;

}
