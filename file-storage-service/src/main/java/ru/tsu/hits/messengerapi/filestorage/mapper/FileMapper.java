package ru.tsu.hits.messengerapi.filestorage.mapper;

import com.ibm.icu.text.Transliterator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.tsu.hits.messengerapi.common.dto.FileDto;
import ru.tsu.hits.messengerapi.filestorage.entity.FileEntity;

import java.time.LocalDateTime;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class FileMapper {

    private final Transliterator transliterator;

    /**
     * Метод для создания объекта FileEntity на основе переданных параметров.
     *
     * @param authorId идентификатор автора файла
     * @param filename имя файла
     * @return объект FileEntity
     */
    public FileEntity buildFileEntity(UUID authorId, String filename) {
        return FileEntity
                .builder()
                .id(UUID.randomUUID())
                .name(convertToFilename(filename))
                .uploadDateTime(LocalDateTime.now())
                .authorId(authorId)
                .build();
    }

    /**
     * Метод для преобразования объекта FileEntity в объект FileDto.
     *
     * @param entity объект FileEntity
     * @return объект FileDto
     */
    public FileDto fileEntityToDto(FileEntity entity){
        return new FileDto(
                entity.getId(),
                entity.getName(),
                entity.getAuthorId(),
                entity.getUploadDateTime()
        );
    }

    /**
     * Метод для преобразования имени файла в корректный формат.
     *
     * @param rawFileName исходное имя файла
     * @return имя файла в корректном формате
     */
    public String convertToFilename(String rawFileName) {
        rawFileName = rawFileName.replace(" ", "_");
        return transliterator.transliterate(rawFileName);
    }
}
