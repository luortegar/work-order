package cl.creando.skappserver.common.service;

import cl.creando.skappserver.common.exception.SKException;
import cl.creando.skappserver.common.entity.user.Notification;
import cl.creando.skappserver.common.entity.user.User;
import cl.creando.skappserver.common.repository.NotificationRepository;
import cl.creando.skappserver.common.repository.UserRepository;
import cl.creando.skappserver.common.request.NotificationPatchRequest;
import cl.creando.skappserver.common.response.AppResponse;
import cl.creando.skappserver.common.response.NotificationResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class NotificationService {
    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;

    public List<NotificationResponse> listNotificationByUserId(String userId) {
        if (userId == null) {
            throw new SKException("Invalid userId.", HttpStatus.BAD_REQUEST);
        }
        UUID userUUID = UUID.fromString(userId);
        User user = userRepository.findById(userUUID).orElseThrow(() -> new SKException("User not found.", HttpStatus.NOT_FOUND));
        List<Notification> notificationList = notificationRepository.findAllByUserAndNotificationViewed(user, false);
        return notificationList.stream().map(NotificationResponse::new).toList();
    }

    public AppResponse markNotificationAsViewed(String notificationId, NotificationPatchRequest notificationRequest) {
        if (notificationId == null) {
            throw new SKException("Invalid notificationId.", HttpStatus.BAD_REQUEST);
        }
        UUID notificationUUI = UUID.fromString(notificationId);
        Notification notification = notificationRepository.findById(notificationUUI).orElseThrow(() -> new SKException("User not found.", HttpStatus.NOT_FOUND));
        notification.setNotificationViewed(notificationRequest.getNotificationViewed());
        AppResponse appResponse = new AppResponse();
        appResponse.setResponseDate(new Date());
        appResponse.setMessage("Notification marked as viewed as successfully.");
        appResponse.setStatusCode(HttpStatus.OK.value());

        return appResponse;
    }
}
