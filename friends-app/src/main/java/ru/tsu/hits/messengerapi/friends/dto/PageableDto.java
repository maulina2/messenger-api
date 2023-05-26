package ru.tsu.hits.messengerapi.friends.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.tsu.hits.messengerapi.common.annotation.PageNumberValidation;
import ru.tsu.hits.messengerapi.common.annotation.PageSizeValidation;

import javax.validation.constraints.NotNull;

/**
 * Дто, в которой передается информация о пагинации.
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class PageableDto {
    @NotNull @PageNumberValidation
    private int pageNumber;
    @NotNull @PageSizeValidation
    private int pageSize;

}
