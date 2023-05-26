package ru.tsu.hits.messengerapi.filestorage.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.tsu.hits.messengerapi.filestorage.entity.FileEntity;

import java.util.UUID;

/**
 * Репозиторий для работы с сущностью FileEntity
 */
public interface FileRepository extends JpaRepository<FileEntity, UUID> {
}
