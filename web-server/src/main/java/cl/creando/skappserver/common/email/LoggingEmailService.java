package cl.creando.skappserver.common.email;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Service
@Profile("test")
public class LoggingEmailService implements EmailService {

    private static final Logger log = LoggerFactory.getLogger(LoggingEmailService.class);

    @Override
    public void sendSimpleMail(String to, String subject, String text) {
        log.info("Simulando envío de correo:");
        log.info("De: {}", "luortegar@gmail.com");
        log.info("Para: {}", to);
        log.info("Asunto: {}", subject);
        log.info("Contenido:\n{}", text);
    }
}
