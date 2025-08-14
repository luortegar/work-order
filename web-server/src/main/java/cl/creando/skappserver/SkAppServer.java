package cl.creando.skappserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScan(basePackages = "cl.creando.skappserver")
@EnableJpaRepositories(basePackages = {"cl.creando.skappserver.common", "cl.creando.skappserver.workorder"})
public class SkAppServer {
    public static void main(String[] args) {
        SpringApplication.run(SkAppServer.class, args);
    }
}
