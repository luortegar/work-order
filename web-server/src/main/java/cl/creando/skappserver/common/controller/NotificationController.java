package cl.creando.skappserver.common.controller;

import cl.creando.skappserver.common.request.NotificationPatchRequest;
import cl.creando.skappserver.common.service.NotificationService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/private/v1/notifications")
@AllArgsConstructor
public class NotificationController {
    private final NotificationService notificationService;

    @GetMapping
    public ResponseEntity<?> listNotificationByUserId(@RequestParam(value = "userId", required = true) String userId) {
        return ResponseEntity.ok(notificationService.listNotificationByUserId(userId));
    }

    @PatchMapping("/{notificationId}/notification-viewed")
    public ResponseEntity<?> markNotificationAsViewed(@PathVariable String notificationId, NotificationPatchRequest notificationRequest) {
        return ResponseEntity.ok(notificationService.markNotificationAsViewed(notificationId, notificationRequest));
    }
}
