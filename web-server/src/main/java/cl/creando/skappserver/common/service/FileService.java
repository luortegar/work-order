package cl.creando.skappserver.common.service;

import cl.creando.skappserver.common.properties.StartedKitProperties;
import cl.creando.skappserver.common.exception.SKException;
import cl.creando.skappserver.common.entity.common.File;
import cl.creando.skappserver.common.repository.FileRepository;
import cl.creando.skappserver.common.response.FileResponse;
import cl.creando.skappserver.common.response.GenericResponse;
import cl.creando.skappserver.common.storage.FileStorageConfig;
import cl.creando.skappserver.common.storage.FileStorageService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j; // Lombok logger
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

@Service
@AllArgsConstructor
@Slf4j
public class FileService {

    private final StartedKitProperties startedKitProperties;
    private final FileRepository fileRepository;
    private final FileStorageConfig fileStorageConfig;

    @Transactional
    public FileResponse saveFile(MultipartFile file) {
        log.info("Starting file save: {}", file.getOriginalFilename());

        FileStorageService fileStorageService = fileStorageConfig.fileStorageService();
        try {
            String filePathId = UUID.randomUUID() + "-" + file.getOriginalFilename();
            log.debug("Generated filePathId: {}", filePathId);

            String fileFullPath = fileStorageService.saveFile(file, filePathId);
            log.debug("File stored at physical path: {}", fileFullPath);

            File fileRecord = new File();
            fileRecord.setFileId(UUID.randomUUID());
            fileRecord.setFilePath(filePathId);
            fileRecord.setFileName(file.getOriginalFilename());
            fileRecord.setContentType(file.getContentType());

            File savedFile = fileRepository.save(fileRecord);
            log.info("File saved in database with ID: {}", savedFile.getFileId());

            return new FileResponse(savedFile);
        } catch (IOException e) {
            log.error("Error saving file: {}", file.getOriginalFilename(), e);
            throw new RuntimeException(e);
        }
    }

    public FileResponse findByFileId(UUID id) {
        log.info("Searching file by ID: {}", id);
        return new FileResponse(fileRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("File not found with ID: {}", id);
                    return new SKException("File not found.", HttpStatus.NOT_FOUND);
                }));
    }

    public GenericResponse deleteFile(UUID id) {
        log.info("Deleting file with ID: {}", id);

        FileStorageService fileStorageService = fileStorageConfig.fileStorageService();
        File file = fileRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("File not found for deletion: {}", id);
                    return new SKException("File not found", HttpStatus.NOT_FOUND);
                });

        try {
            fileStorageService.deleteFile(file.getFilePath());
            log.debug("File deleted from storage: {}", file.getFilePath());
        } catch (IOException e) {
            log.error("Error deleting file from storage: {}", file.getFilePath(), e);
            throw new SKException("Error deleting file: " + file.getFilePath(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

        GenericResponse genericResponse = new GenericResponse();
        genericResponse.setMessage("File deleted successfully.");
        log.info("File deleted successfully with ID: {}", id);
        return genericResponse;
    }

    public ResponseEntity<Resource> downloadFile(UUID id) {
        log.info("Downloading file with ID: {}", id);

        FileStorageService fileStorageService = fileStorageConfig.fileStorageService();
        File file = fileRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("File not found for download: {}", id);
                    return new SKException("File not found", HttpStatus.NOT_FOUND);
                });

        try {
            InputStream inputStream = fileStorageService.getFile(file.getFilePath());
            InputStreamResource resource = new InputStreamResource(inputStream);

            log.info("File ready for download: {}", file.getFileName());

            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(file.getContentType()))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFileName() + "\"")
                    .body(resource);
        } catch (IOException e) {
            log.error("Error reading file from storage: {}", file.getFilePath(), e);
            throw new SKException("File not found or not readable: " + file.getFilePath(), HttpStatus.NOT_FOUND);
        }
    }
}
