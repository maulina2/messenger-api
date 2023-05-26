package ru.tsu.hits.messengerapi.chat.mapper;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.tsu.hits.messengerapi.chat.dto.AttachmentDto;
import ru.tsu.hits.messengerapi.chat.entity.AttachmentEntity;
import ru.tsu.hits.messengerapi.chat.entity.MessageEntity;
import ru.tsu.hits.messengerapi.common.dto.FileDto;

/**
 * Класс для маппинга различных dto и {@link AttachmentEntity}.
 */
@Component
@RequiredArgsConstructor
public class AttachmentMapper {

    /**
     * Метод для создания объекта AttachmentEntity на основе переданных параметров.
     *
     * @param message объект MessageEntity, к которому будет прикреплен файл
     * @param dto объект FileDto, содержащий информацию о файле
     * @return объект AttachmentEntity с заполненными полями message, name и fileId
     */
    public AttachmentEntity attachmentDtoToEntity(MessageEntity message, FileDto dto) {
        return AttachmentEntity
                .builder()
                .message(message)
                .name(dto.getName())
                .fileId(dto.getFileId())
                .build();
    }

    /**
     * Метод для преобразования объекта AttachmentEntity в объект AttachmentDto.
     *
     * @param entity объект AttachmentEntity, который необходимо преобразовать
     * @return объект AttachmentDto с заполненными полями name и fileId
     */
    public AttachmentDto attachmentEntityToDto(AttachmentEntity entity){
        return new AttachmentDto(
                entity.getName(),
                entity.getFileId()
        );
    }
}
