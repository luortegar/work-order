package cl.creando.skappserver.common.service;

import cl.creando.skappserver.common.entity.common.File;
import cl.creando.skappserver.common.exception.SKException;
import cl.creando.skappserver.common.properties.StartedKitProperties;
import cl.creando.skappserver.common.repository.FileRepository;
import cl.creando.skappserver.common.response.FileResponse;
import cl.creando.skappserver.common.response.GenericResponse;
import cl.creando.skappserver.common.storage.FileStorageConfig;
import cl.creando.skappserver.common.storage.FileStorageService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Optional;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

        try {
            File savedFile = getSavedFile(file);
            log.info("File saved in database with ID: {}", savedFile.getFileId());

            return new FileResponse(savedFile);
        } catch (IOException e) {
            log.error("Error saving file: {}", file.getOriginalFilename(), e);
            throw new RuntimeException(e);
        }
    }

    public File getSavedFile(MultipartFile file) throws IOException {
        FileStorageService fileStorageService = fileStorageConfig.fileStorageService();

        String filePathId = UUID.randomUUID() + "-" + file.getOriginalFilename();
        log.debug("Generated filePathId: {}", filePathId);

        String fileFullPath = fileStorageService.saveFile(file, filePathId);
        log.debug("File stored at physical path: {}", fileFullPath);

        File fileRecord = new File();
        fileRecord.setFileId(UUID.randomUUID());
        fileRecord.setFilePath(filePathId);
        fileRecord.setFileName(file.getOriginalFilename());
        fileRecord.setContentType(file.getContentType());

        return fileRepository.save(fileRecord);
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
            //throw new SKException("Error deleting file: " + file.getFilePath(), HttpStatus.INTERNAL_SERVER_ERROR);
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

    public Optional<File> findById(UUID photoId) {
        return fileRepository.findById(photoId);
    }

    public String getFileAsBase64(UUID id) {
        log.info("Fetching file as Base64 with ID: {}", id);

        FileStorageService fileStorageService = fileStorageConfig.fileStorageService();
        File file = fileRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("File not found for Base64 conversion: {}", id);
                    return new SKException("File not found", HttpStatus.NOT_FOUND);
                });

        try (InputStream inputStream = fileStorageService.getFile(file.getFilePath())) {

            // Leer todo el contenido a un byte[]
            byte[] fileBytes = inputStream.readAllBytes();   // Java 9+

            // Codificar en Base64
            String base64Data = Base64.getEncoder().encodeToString(fileBytes);

            // Preparar el prefijo con el content-type del archivo
            String contentType = file.getContentType() != null
                    ? file.getContentType()
                    : "application/octet-stream";

            // Construir el Data URL estándar
            String base64WithType = "data:" + contentType + ";base64," + base64Data;

            log.info("File successfully converted to Base64 with type: {} -> {}",
                    file.getFileName(), contentType);

            return base64WithType;

        } catch (IOException e) {
            log.error("Error reading file for Base64 conversion: {}", file.getFilePath(), e);
            throw new SKException("File not readable: " + file.getFilePath(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    public FileResponse getFileResponse(UUID referenceId, File file) {
        return new FileResponse(file, startedKitProperties.getBase().getApiUrl(), referenceId);
    }

    public File saveFileBase64(String recipientSignatureBase64, String name) {
        log.info("Starting Base64 file save: {}", name);

        try {
            // Si el base64 viene con encabezado tipo "data:image/png;base64,...."
            // separamos el prefijo para extraer el contentType
            String base64Data = recipientSignatureBase64;
            String contentType = "application/octet-stream"; // valor por defecto

            Pattern pattern = Pattern.compile("^data:(.+?);base64,(.+)$");
            Matcher matcher = pattern.matcher(recipientSignatureBase64);
            if (matcher.find()) {
                contentType = matcher.group(1);
                base64Data = matcher.group(2);
            }

            // Decodificar base64 a bytes
            byte[] fileBytes = Base64.getDecoder().decode(base64Data.getBytes(StandardCharsets.UTF_8));

            // Usamos ByteArrayInputStream para no crear archivo temporal
            FileStorageService fileStorageService = fileStorageConfig.fileStorageService();
            String filePathId = UUID.randomUUID() + "-" + name;
            log.debug("Generated filePathId for Base64: {}", filePathId);

            // Guardar el archivo en el almacenamiento físico
            try (ByteArrayInputStream bais = new ByteArrayInputStream(fileBytes)) {
                MultipartFile multipartFile = new ByteArrayMultipartFile(
                        bais.readAllBytes(),                  // contenido
                        "file",                 // name
                        name,          // nombre original
                        contentType             // content type
                );
                fileStorageService.saveFile(multipartFile, filePathId); // necesitas un método sobrecargado que acepte InputStream y filePathId
            }

            // Guardar registro en la base de datos
            File fileRecord = new File();
            fileRecord.setFileId(UUID.randomUUID());
            fileRecord.setFilePath(filePathId);
            fileRecord.setFileName(name);
            fileRecord.setContentType(contentType);

            File savedFile = fileRepository.save(fileRecord);
            log.info("Base64 file saved in database with ID: {}", savedFile.getFileId());

            return savedFile;

        } catch (IOException e) {
            log.error("Error saving Base64 file: {}", name, e);
            throw new SKException("Error saving Base64 file", HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (IllegalArgumentException e) {
            log.error("Invalid Base64 content for file: {}", name, e);
            throw new SKException("Invalid Base64 content", HttpStatus.BAD_REQUEST);
        }
    }
}
