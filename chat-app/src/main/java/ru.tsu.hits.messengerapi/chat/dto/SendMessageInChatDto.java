package ru.tsu.hits.messengerapi.chat.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;
import java.util.UUID;

/**
 * Дто, в которой передается информация для отправки сообщения в чат.
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class SendMessageInChatDto {
    @NotNull
    private UUID chatId;
    @NotNull(message = "В сообщении должно быть что-то написано")
    @NotBlank(message = "В сообщении должно быть что-то написано")
    @Size(max = 500, message = "Длина сообщения не может превышать 500 символов")
    private String text;

    @Size(max = 10, message = "Количество вложений не может быть больше 10")
    private List<UUID> fileIds;
}
