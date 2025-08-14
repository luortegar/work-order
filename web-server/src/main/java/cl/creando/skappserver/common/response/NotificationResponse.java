package cl.creando.skappserver.common.response;


import cl.creando.skappserver.common.entity.user.Notification;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class NotificationResponse {

    private UUID notificationId;
    private String message;
    private Notification.NotificationType notificationType;
    private Boolean notificationViewed;

    public NotificationResponse(Notification n) {
        this.notificationId = n.getNotificationId();
        this.message = n.getMessage();
        this.notificationType = n.getNotificationType();
        this.notificationViewed = n.getNotificationViewed();
    }
}
