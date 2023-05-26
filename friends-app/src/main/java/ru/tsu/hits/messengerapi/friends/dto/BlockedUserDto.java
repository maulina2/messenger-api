package ru.tsu.hits.messengerapi.friends.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.UUID;

/**
 * Дто, в которой передается информация об BlockedUserEntity.
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class BlockedUserDto {

    private UUID id;

    private String fullName;

    private UUID externalUser ;

    private Date blockedUserAddedDate;

    private Date blockedUserRemovalDate;
}
