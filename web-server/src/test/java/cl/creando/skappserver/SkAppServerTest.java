package cl.creando.skappserver;

import cl.creando.skappserver.common.email.EmailService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ApplicationContextTest {

    @Autowired
    private EmailService javaMailSender;


    @Test
    void contextLoads() {
        javaMailSender.sendSimpleMail("luortegar@gmail.com","test", "super duper");

    }
}
