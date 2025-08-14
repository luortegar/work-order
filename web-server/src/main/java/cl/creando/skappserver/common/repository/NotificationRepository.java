package cl.creando.skappserver.common.repository;

import cl.creando.skappserver.common.entity.user.Notification;
import cl.creando.skappserver.common.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;
@Repository
public interface NotificationRepository extends JpaRepository<Notification, UUID> {
    List<Notification> findAllByUserAndNotificationViewed(User user, Boolean notificationViewed);
}
