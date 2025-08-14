package cl.creando.skappserver.common.storage;

import cl.creando.skappserver.common.properties.StartedKitProperties;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
@AllArgsConstructor
public class LocalFileStorageService implements FileStorageService {

    private final StartedKitProperties properties;

    @Override
    public String saveFile(MultipartFile file, String fileId) throws IOException {
        Path path = Paths.get(properties.getFile().getLocal().getPath(), fileId);
        Files.write(path, file.getBytes());
        return path.toString();
    }

    @Override
    public InputStream getFile(String fileName) throws IOException {
        Path path = Paths.get(properties.getFile().getLocal().getPath(), fileName);
        return Files.newInputStream(path);
    }

    @Override
    public void deleteFile(String fileId) throws IOException {
        Path path = Paths.get(properties.getFile().getLocal().getPath(), fileId);
        Files.delete(path);
    }
}