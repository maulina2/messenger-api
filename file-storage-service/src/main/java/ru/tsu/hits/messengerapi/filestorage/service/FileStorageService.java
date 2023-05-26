package ru.tsu.hits.messengerapi.filestorage.service;

import io.minio.GetObjectArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import ru.tsu.hits.messengerapi.common.dto.FileDto;
import ru.tsu.hits.messengerapi.common.exception.InternalException;
import ru.tsu.hits.messengerapi.common.exception.NotFoundException;
import ru.tsu.hits.messengerapi.filestorage.config.MinioConfig;
import ru.tsu.hits.messengerapi.filestorage.entity.FileEntity;
import ru.tsu.hits.messengerapi.filestorage.mapper.FileMapper;
import ru.tsu.hits.messengerapi.filestorage.repository.FileRepository;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.UUID;


@Slf4j
@Service
@RequiredArgsConstructor
public class FileStorageService {

    private final MinioClient minioClient;
    private final MinioConfig minioConfig;
    private final FileRepository fileRepository;
    private final FileMapper fileMapper;

    /**
     * Метод загружает файл в хранилище и сохраняет метаданные файла в базе данных.
     *
     * @param authorId идентификатор автора файла
     * @param file     загружаемый файл
     * @return объект FileDto, содержащий метаданные загруженного файла
     * @throws InternalException если произошла ошибка во время загрузки файла в хранилище
     */
    @Transactional
    public FileDto uploadFile(UUID authorId, MultipartFile file) {
        try {
            FileEntity fileEntity = fileMapper.buildFileEntity(authorId, file.getOriginalFilename());

            PutObjectArgs putObjectArgs = buildObject(fileEntity, file);
            minioClient.putObject(putObjectArgs);

            fileEntity = fileRepository.save(fileEntity);
            return fileMapper.fileEntityToDto(fileEntity);
        } catch (Exception exception) {
            throw new InternalException("Ошибка во время загрузки файла в хранилище", exception);
        }
    }

    /**
     * Метод создает объект PutObjectArgs для загрузки файла в хранилище.
     *
     * @param metaData метаданные загружаемого файла
     * @param file     загружаемый файл
     * @return объект PutObjectArgs для загрузки файла в хранилище
     * @throws IOException если произошла ошибка ввода-вывода при чтении содержимого файла
     */
    private PutObjectArgs buildObject(FileEntity metaData, MultipartFile file) throws IOException {
        byte[] content = file.getBytes();

        return PutObjectArgs
                .builder()
                .bucket(minioConfig.getBucket())
                .object(metaData.getId().toString())
                .stream(new ByteArrayInputStream(content), content.length, -1)
                .build();
    }

    /**
     * Метод скачивает файл из хранилища и возвращает его содержимое в виде массива байтов.
     *
     * @param fileId идентификатор скачиваемого файла
     * @return объект Pair, содержащий имя и содержимое скачиваемого файла
     * @throws NotFoundException  если файл с указанным идентификатором не найден
     * @throws InternalException  если произошла ошибка при скачивании файла из хранилища
     */
    @Transactional(readOnly = true)
    public Pair<String, byte[]> downloadFile(UUID fileId) {
        String filename = fileRepository
                .findById(fileId)
                .orElseThrow(() -> new NotFoundException(String.format("Файл с id = '%s' не найден", fileId)))
                .getName();

        GetObjectArgs getObjectArgs = GetObjectArgs
                .builder()
                .bucket(minioConfig.getBucket())
                .object(fileId.toString())
                .build();

        try (var in = minioClient.getObject(getObjectArgs)) {
            return Pair.of(filename, in.readAllBytes());
        } catch (Exception e) {
            throw new InternalException("Ошибка при скачивании файла из S3 c id = " + fileId);
        }

    }

    /**
     * Метод возвращает метаданные файла по его идентификатору.
     *
     * @param fileId идентификатор файла
     * @return объект FileDto, содержащий метаданные файла
     * @throws NotFoundException если файл с указанным идентификатором не найден
     */
    @Transactional(readOnly = true)
    public FileDto getFileData(UUID fileId) {
        FileEntity fileMetaData = fileRepository
                .findById(fileId)
                .orElseThrow(() -> new NotFoundException(String.format("Файл с id = '%s' не найден", fileId)));

        return fileMapper.fileEntityToDto(fileMetaData);
    }
}
