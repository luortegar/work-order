package cl.creando.skappserver.common.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "started-kit")
public class StartedKitProperties {

    private Base base = new Base();
    private FileConfig file = new FileConfig();

    @Data
    public static class FileConfig {
        private String storage = "local";
        private Local local = new Local();
        private S3 s3 = new S3();
        private Gcp gcp = new Gcp();
    }

    @Data
    public static class Local {
        private String path = "/Users/luortegar/Github/easy-demo/easy-demo-server/files/";
    }

    @Data
    public static class S3 {
        private String bucketName = "default-bucket";
        private String region = "us-east-1";
        private String accessKey = "default-access-key";
        private String secretKey = "default-secret-key";
    }

    @Data
    public static class Base {
        private String apiUrl = "http://localhost:1010/started-kit";
        private String uiUrl = "http://localhost:3030";
    }

    @Data
    public static class Gcp {
        private String bucketName = "default-bucket";
        private String serviceAccountJson = "";
    }
}