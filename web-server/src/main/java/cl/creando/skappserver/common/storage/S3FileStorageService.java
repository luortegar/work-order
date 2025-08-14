package cl.creando.skappserver.common.storage;

import cl.creando.skappserver.common.properties.StartedKitProperties;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.ResponseBytes;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.io.InputStream;

@Service
@AllArgsConstructor
public class S3FileStorageService implements FileStorageService {

    private final StartedKitProperties properties;

    @Override
    public String saveFile(MultipartFile file, String fileId) throws IOException {
        AwsCredentials credentials = AwsBasicCredentials
                .create(properties.getFile().getS3().getAccessKey(), properties.getFile().getS3().getSecretKey());

        S3Client s3Client = S3Client.builder()
                .credentialsProvider(StaticCredentialsProvider.create(credentials))
                .build();

        s3Client.putObject(PutObjectRequest.builder()
                        .bucket(properties.getFile().getS3().getBucketName())
                        .key(fileId)
                        .build(),
                RequestBody.fromBytes(file.getBytes()));
        return fileId;
    }

    @Override
    public InputStream getFile(String fileId) {
        AwsCredentials credentials = AwsBasicCredentials
                .create(properties.getFile().getS3().getAccessKey(), properties.getFile().getS3().getSecretKey());

        S3Client s3Client = S3Client.builder()
                .credentialsProvider(StaticCredentialsProvider.create(credentials))
                .build();

        ResponseBytes<GetObjectResponse> objectBytes = s3Client.getObjectAsBytes(GetObjectRequest.builder()
                .bucket(properties.getFile().getS3().getBucketName())
                .key(fileId)
                .build());

        return objectBytes.asInputStream();
    }

    @Override
    public void deleteFile(String fileId) throws IOException {
        AwsCredentials credentials = AwsBasicCredentials
                .create(properties.getFile().getS3().getAccessKey(), properties.getFile().getS3().getSecretKey());

        S3Client s3Client = S3Client.builder()
                .credentialsProvider(StaticCredentialsProvider.create(credentials))
                .build();

        s3Client.deleteObject(builder -> builder
                .bucket(properties.getFile().getS3().getBucketName())
                .key(fileId)
                .build());
    }
}