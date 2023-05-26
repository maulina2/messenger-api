package ru.tsu.hits.messengerapi.notification.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.tsu.hits.messengerapi.common.dto.PageableDto;

import javax.validation.Valid;


/**
 * Дто, в которой передается информация для пагинации списка уведомлений
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class PageDto {

    @Valid
    private PageableDto pageableDto;

    private NotificationFiltersDto notificationFiltersDto;
}
