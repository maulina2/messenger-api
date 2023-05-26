package ru.tsu.hits.messengerapi.filestorage.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.util.Pair;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.tsu.hits.messengerapi.common.dto.FileDto;
import ru.tsu.hits.messengerapi.filestorage.service.FileStorageService;

import java.util.UUID;

import static ru.tsu.hits.messengerapi.commonsecurity.service.JwtService.extractId;

@RestController
@RequestMapping("/api/v1/file-storage")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Файлы")
public class FileStorageController {

    private final FileStorageService fileStorageService;

    /**
     * Метод для загрузки файла в хранилище.
     *
     * @param auth объект аутентификации пользователя
     * @param file загружаемый файл
     * @return объект FileDto, содержащий информацию о загруженном файле
     */
    @PostMapping(
            value = "/upload",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @Operation(
            summary = "Загрузить файл в хранилище.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    public FileDto uploadFile(Authentication auth,
                              @RequestParam("file") MultipartFile file) {
        return fileStorageService.uploadFile(extractId(auth), file);
    }

    /**
     * Метод для скачивания файла.
     *
     * @param fileId идентификатор файла
     * @return объект ResponseEntity с массивом байтов файла и его именем
     */
    @GetMapping(value = "/download/{fileId}", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    @Operation(
            summary = "Скачать файл.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    public ResponseEntity<byte[]> downloadFile(@PathVariable @NonNull UUID fileId) {
        Pair<String, byte[]> fileAndFilename = fileStorageService.downloadFile(fileId);

        return ResponseEntity
                .ok()
                .header(
                        "Content-Disposition",
                        String.format("filename=%s", fileAndFilename.getFirst())
                )
                .body(fileAndFilename.getSecond());
    }

    /**
     * Метод для получения информации о файле.
     *
     * @param fileId идентификатор файла
     * @return объект FileDto, содержащий информацию о файле
     */
    @GetMapping(value = "/{fileId}")
    @Operation(
            summary = "Получить информацию о файле.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    public FileDto getFileMetaDataDto(@PathVariable @NonNull UUID fileId) {
        return fileStorageService.getFileData(fileId);
    }
}
