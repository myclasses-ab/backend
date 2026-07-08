package com.classes.Backend.Controller.notification;

import com.classes.Backend.Domain.enums.NotificationType;
import com.classes.Backend.Domain.notification.Notification;
import com.classes.Backend.Service.notification.NotificationServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/notifications")
public class NotificationController {

    private final NotificationServiceImpl NOTIFICATION_SERVICE_IMPL;

    // ================ CREATE NOTIFICATION ===================== //
    @PostMapping
    public ResponseEntity<?> saveNotification(@RequestBody Notification notification) {
        return new ResponseEntity<>(this.NOTIFICATION_SERVICE_IMPL.save(notification), HttpStatus.CREATED);
    }

    // ================ CREATE ALL NOTIFICATIONS ===================== //
    @PostMapping("/bulk")
    public ResponseEntity<?> saveAllNotifications(@RequestBody List<Notification> notifications) {
        return new ResponseEntity<>(this.NOTIFICATION_SERVICE_IMPL.saveAll(notifications), HttpStatus.CREATED);
    }

    // ================ GET NOTIFICATION BY ID ===================== //
    @GetMapping("/{identifier}")
    public ResponseEntity<?> getNotificationById(@PathVariable String identifier) {
        return new ResponseEntity<>(this.NOTIFICATION_SERVICE_IMPL.findById(identifier), HttpStatus.OK);
    }

    // ================ GET ALL NOTIFICATIONS ===================== //
    @GetMapping
    public ResponseEntity<?> getAllNotifications() {
        List<Notification> allNotifications = this.NOTIFICATION_SERVICE_IMPL.findAll();
        return new ResponseEntity<>(allNotifications, HttpStatus.OK);
    }

    // ================ DELETE NOTIFICATION BY ID ===================== //
    @DeleteMapping("/{identifier}")
    public ResponseEntity<?> deleteNotificationById(@PathVariable String identifier) {
        this.NOTIFICATION_SERVICE_IMPL.deleteById(identifier);
        return new ResponseEntity<>("Notification deleted successfully", HttpStatus.OK);
    }

    // ================ UPDATE NOTIFICATIONTYPE BY ID ===================== //
    @PutMapping("/{identifier}")
    public ResponseEntity<?> updateNotificationById(@PathVariable String identifier, @RequestBody Notification notification) {
        if (!this.NOTIFICATION_SERVICE_IMPL.existsById(identifier)) {
            return new ResponseEntity<>("NotificationType not found", HttpStatus.NOT_FOUND);
        }
        notification.setIdentifier(identifier);
        return new ResponseEntity<>(this.NOTIFICATION_SERVICE_IMPL.save(notification), HttpStatus.OK);
    }

    // ================ FIND BY USER IDENTIFIER ===================== //
    @GetMapping("/user/{userIdentifier}")
    public ResponseEntity<?> findByUserIdentifier(@PathVariable String userIdentifier) {
        return new ResponseEntity<>(this.NOTIFICATION_SERVICE_IMPL.findByUserIdentifier(userIdentifier), HttpStatus.OK);
    }

    // ================ FIND UNREAD NOTIFICATIONS ===================== //
    @GetMapping("/user/{userIdentifier}/unread")
    public ResponseEntity<?> findByUserIdentifierAndIsReadFalse(@PathVariable String userIdentifier) {
        return new ResponseEntity<>(this.NOTIFICATION_SERVICE_IMPL.findByUserIdentifierAndIsReadFalse(userIdentifier), HttpStatus.OK);
    }

    // ================ FIND BY TYPE ===================== //
    @GetMapping("/type/{type}")
    public ResponseEntity<?> findByType(@PathVariable NotificationType type) {
        return new ResponseEntity<>(this.NOTIFICATION_SERVICE_IMPL.findByType(type), HttpStatus.OK);
    }

    // ================ MARK AS READ ===================== //
    @PatchMapping("/{identifier}/read")
    public ResponseEntity<?> markAsRead(@PathVariable String identifier) {
        return new ResponseEntity<>(this.NOTIFICATION_SERVICE_IMPL.markAsRead(identifier), HttpStatus.OK);
    }
}
