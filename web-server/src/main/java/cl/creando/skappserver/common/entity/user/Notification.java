package cl.creando.skappserver.common.entity.user;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;

import java.util.UUID;

import static org.hibernate.annotations.UuidGenerator.Style.TIME;

@Entity
@Table(name = "tbl_notification")
@Getter
@Setter
public class Notification {
    @Id
    @GeneratedValue
    @UuidGenerator(style = TIME)
    private UUID notificationId;
    private String message;
    private NotificationType notificationType;
    private Boolean notificationViewed;

    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
    @JoinColumn(name = "userId")
    private User user;

    public enum NotificationType {
        INFO, WARNING, ERROR
    }
}
