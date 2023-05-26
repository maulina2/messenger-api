package ru.tsu.hits.messengerapi.friends.entity;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Date;
import java.util.UUID;

/**
 * Сущность пользователя, которого добавили в черный список.
 */
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Table(name = "black_list")
public class BlockedUserEntity {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    private UUID id;

    private String fullName;

    private UUID targetUser;

    private UUID externalUser ;

    @CreationTimestamp
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @Temporal(TemporalType.DATE)
    private Date blockedUserAddedDate;

    @Temporal(TemporalType.DATE)
    private Date blockedUserRemovalDate;

    public BlockedUserEntity(String fullName, UUID externalUser, Date blockedUserAddedDate, Date blockedUserRemovalDate, UUID targetUser) {
        this.fullName = fullName;
        this.externalUser = externalUser;
        this.blockedUserAddedDate = blockedUserAddedDate;
        this.blockedUserRemovalDate = blockedUserRemovalDate;
        this.targetUser = targetUser;
    }

    public static BlockedUserEntity from(String fullName, UUID externalUser, Date blockedUserAddedDate, Date blockedUserRemovalDate, UUID targetUser) {
        return new BlockedUserEntity(fullName, externalUser, blockedUserAddedDate, blockedUserRemovalDate, targetUser);
    }
}
