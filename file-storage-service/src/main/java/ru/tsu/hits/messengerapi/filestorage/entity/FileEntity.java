package ru.tsu.hits.messengerapi.filestorage.entity;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Сущность информации о файле.
 */
@Entity
@Table(name = "file_data")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FileEntity {
    @Id
    private UUID id;

    private String name;

    private LocalDateTime uploadDateTime;

    private UUID authorId;
}
