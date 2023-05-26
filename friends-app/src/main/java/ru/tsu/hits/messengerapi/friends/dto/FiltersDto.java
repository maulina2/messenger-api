package ru.tsu.hits.messengerapi.friends.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;
import java.util.UUID;

/**
 * Дто, в которой передаются фильтры для поиска пользователя.
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class FiltersDto {
    private String fullName;

    private UUID externalUser ;
    @Temporal(TemporalType.DATE)
    private Date addedDate;
    @Temporal(TemporalType.DATE)
    private Date removalDate;
}
