package com.classes.Backend.Service.notification;

import com.classes.Backend.Domain.notification.Notification;
import com.classes.Backend.Domain.enums.NotificationType;

import java.util.List;
import java.util.Optional;

public interface NotificationService {
    // ================ CRUD OPERATIONS ===================== //
    Notification save(Notification notification);
    List<Notification> saveAll(List<Notification> notifications);
    Optional<Notification> findById(String identifier);
    List<Notification> findAll();
    void deleteById(String identifier);
    boolean existsById(String identifier);

    // ================ CUSTOM FINDER METHODS ===================== //
    List<Notification> findByUserIdentifier(String userIdentifier);
    List<Notification> findByUserIdentifierAndIsReadFalse(String userIdentifier);
    List<Notification> findByType(NotificationType type);
}
