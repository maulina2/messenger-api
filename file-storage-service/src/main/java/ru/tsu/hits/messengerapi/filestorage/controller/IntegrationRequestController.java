package ru.tsu.hits.messengerapi.filestorage.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.tsu.hits.messengerapi.common.dto.FileDto;
import ru.tsu.hits.messengerapi.common.util.Constants;
import ru.tsu.hits.messengerapi.filestorage.service.FileStorageService;

import java.util.UUID;

@RestController
@RequestMapping("/integration/file-storage")
@RequiredArgsConstructor
@Tag(name = "Интеграционные запросы")
public class IntegrationRequestController {

    private final FileStorageService fileStorageService;

    /**
     * Метод для получения информации о файле.
     *
     * @param fileId идентификатор файла
     * @return объект FileDto, содержащий информацию о файле
     */
    @GetMapping("/{fileId}")
    @Operation(
            summary = "Получить метаинформацию о пользователе.",
            security = @SecurityRequirement(name = Constants.API_KEY)
    )
    public FileDto getFileMetaData(@PathVariable UUID fileId) {
        return fileStorageService.getFileData(fileId);
    }

}