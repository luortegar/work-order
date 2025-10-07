package cl.creando.skappserver.common.storage;

import cl.creando.skappserver.common.properties.StartedKitProperties;
import com.google.auth.oauth2.ServiceAccountCredentials;
import com.google.cloud.storage.*;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

@Service
@AllArgsConstructor
public class GCPFileStorageService implements FileStorageService {

    private final StartedKitProperties properties;

    private Storage getStorage() throws IOException {
        return StorageOptions.newBuilder()
                .setCredentials(ServiceAccountCredentials.fromStream(new ByteArrayInputStream(properties.getFile().getGcp().getServiceAccountJson().getBytes())))
                .build()
                .getService();
    }

    @Override
    public String saveFile(MultipartFile file, String fileId) throws IOException {
        Storage storage = getStorage();
        BlobId blobId = BlobId.of(properties.getFile().getGcp().getBucketName(), fileId);
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId).build();

        storage.create(blobInfo, file.getBytes());
        return fileId;
    }

    @Override
    public InputStream getFile(String fileId) throws IOException {
        Storage storage = getStorage();
        Blob blob = storage.get(BlobId.of(properties.getFile().getGcp().getBucketName(), fileId));
        return new ByteArrayInputStream(blob.getContent());
    }

    @Override
    public void deleteFile(String fileId) throws IOException {
        Storage storage = getStorage();
        storage.delete(BlobId.of(properties.getFile().getGcp().getBucketName(), fileId));
    }
}
