package ru.tsu.hits.messengerapi.friends.entity;


import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;
import java.util.UUID;

/**
 * Сущность пользователя, добавленного в друзья.
 */
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Table(name = "friend")
public class FriendEntity {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    private UUID id;

    private String fullName;

    private UUID targetUser;

    private UUID externalUser;

    @Temporal(TemporalType.DATE)
    private Date friendAddedDate;

    @Temporal(TemporalType.DATE)
    private Date friendRemovalDate;

    public FriendEntity(String fullName, UUID externalUser, Date friendAddedDate, Date friendRemovalDate, UUID targetUser) {
        this.fullName = fullName;
        this.externalUser = externalUser;
        this.friendAddedDate = friendAddedDate;
        this.friendRemovalDate = friendRemovalDate;
        this.targetUser = targetUser;
    }

    public static FriendEntity from(String fullName, UUID externalUser, Date friendAddedDate, Date friendRemovalDate, UUID targetUser) {
        return new FriendEntity(fullName, externalUser, friendAddedDate, friendRemovalDate, targetUser);
    }
}