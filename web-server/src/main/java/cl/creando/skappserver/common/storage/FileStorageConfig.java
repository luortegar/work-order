package cl.creando.skappserver.common.storage;

import cl.creando.skappserver.common.properties.StartedKitProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FileStorageConfig {

    public static final String S3 = "s3";
    private final StartedKitProperties properties;

    private final LocalFileStorageService localFileStorageService;
    private final S3FileStorageService s3FileStorageService;

    public FileStorageConfig(StartedKitProperties properties, LocalFileStorageService localFileStorageService, S3FileStorageService s3FileStorageService) {
        this.properties = properties;
        this.localFileStorageService = localFileStorageService;

        this.s3FileStorageService = s3FileStorageService;
    }

    @Bean
    public FileStorageService fileStorageService() {
        return S3.equalsIgnoreCase(properties.getFile().getStorage()) ? s3FileStorageService : localFileStorageService;
    }
}