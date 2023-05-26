package ru.tsu.hits.messengerapi.user.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Sort;
import ru.tsu.hits.messengerapi.common.annotation.PageNumberValidation;
import ru.tsu.hits.messengerapi.common.annotation.PageSizeValidation;

import java.util.Map;

/**
 * Дто для пагинации с  фильтрацией, сортировками списка пользователей
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class UserPageDto {

    @PageNumberValidation
    private int pageNumber;
    @PageSizeValidation
    private int pageSize;
    private FiltersDto filters;
    private Map<String, Sort.Direction> sorts;
}
