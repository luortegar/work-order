package cl.creando.skappserver.common.email;

public interface EmailService {
    void sendSimpleMail(String to, String subject, String text);
}
