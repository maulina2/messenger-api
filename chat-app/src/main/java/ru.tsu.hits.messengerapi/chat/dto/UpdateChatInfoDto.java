package ru.tsu.hits.messengerapi.chat.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;


/**
 * Дто, в которой передается информация для обновления чата.
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class UpdateChatInfoDto {

    @NotNull
    private UUID id;

    @NotNull @NotBlank
    private String name;


    private UUID avatarId;

    private List<UUID> usersId;
}
