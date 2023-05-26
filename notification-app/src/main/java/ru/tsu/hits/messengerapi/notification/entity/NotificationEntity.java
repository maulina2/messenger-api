package ru.tsu.hits.messengerapi.notification.entity;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import ru.tsu.hits.messengerapi.common.enumeration.NotificationType;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Сущность уведомления.
 */
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Table(name = "notification")
public class NotificationEntity {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    private UUID id;

    @Enumerated(EnumType.STRING)
    private NotificationType notificationType;

    private String text;

    private UUID userId;

    @JsonFormat(pattern = "yyyy-MM-dd hh:mm")
    private LocalDateTime receivingDate;

    private LocalDateTime readDate;

    private boolean isRead;
}
