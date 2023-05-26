package ru.tsu.hits.messengerapi.notification.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Дто, в которой передается информация о периоде, за который нужно передать список уведомлений
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class PeriodDto {

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime fromDateTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime toDateTime;
}
