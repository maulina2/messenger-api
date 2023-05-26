package ru.tsu.hits.messengerapi.friends.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.UUID;

/**
 * Дто, в которой передается информация об FriendEntity.
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class FriendDto {

    private UUID id;

    private String fullName;

    private UUID externalUser ;

    private Date friendAddedDate;

    private Date friendRemovalDate;
}
