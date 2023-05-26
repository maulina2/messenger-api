package ru.tsu.hits.messengerapi.chat.entity;


import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import ru.tsu.hits.messengerapi.chat.enumeration.ChatType;

import javax.persistence.*;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Сущность чата или диалога
 */
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Table(name = "chat")
public class ChatEntity {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    private UUID id;

    @Enumerated(EnumType.STRING)
    private ChatType chatType;

    @Temporal(TemporalType.DATE)
    private Date chatCreationDate;

    private String name;

    private UUID admin;

    private UUID avatarId;

    @OneToMany(mappedBy = "chat")
    private List<MessageEntity> message;

}
