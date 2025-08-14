package cl.creando.skappserver.common.storage;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

public interface FileStorageService {
    String saveFile(MultipartFile file, String fileId) throws IOException;
    InputStream getFile(String fileId) throws IOException;
    void deleteFile(String fileId) throws IOException;
}
