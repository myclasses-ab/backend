package com.classes.Backend.Service.notification;

import com.classes.Backend.Domain.notification.Notification;
import com.classes.Backend.Domain.enums.NotificationType;
import com.classes.Backend.Repository.notification.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class NotificationServiceImpl implements NotificationService {
    private final NotificationRepository NOTIFICATION_REPOSITORY;

    // ================ SAVE NOTIFICATION ===================== //
    @Override
    public Notification save(Notification notification) {
        return this.NOTIFICATION_REPOSITORY.save(notification);
    }

    // ================ SAVE ALL NOTIFICATIONS ===================== //
    @Override
    public List<Notification> saveAll(List<Notification> notifications) {
        return this.NOTIFICATION_REPOSITORY.saveAll(notifications);
    }

    // ================ FIND BY ID ===================== //
    @Override
    public Optional<Notification> findById(String identifier) {
        return this.NOTIFICATION_REPOSITORY.findById(identifier);
    }

    // ================ FIND ALL ===================== //
    @Override
    public List<Notification> findAll() {
        return this.NOTIFICATION_REPOSITORY.findAll();
    }

    // ================ DELETE BY ID ===================== //
    @Override
    public void deleteById(String identifier) {
        if (!this.NOTIFICATION_REPOSITORY.existsById(identifier)) {
            throw new RuntimeException("Notification with identifier '" + identifier + "' not found");
        }
        this.NOTIFICATION_REPOSITORY.deleteById(identifier);
    }

    // ================ EXISTS BY ID ===================== //
    @Override
    public boolean existsById(String identifier) {
        return this.NOTIFICATION_REPOSITORY.existsById(identifier);
    }

    // ================ FIND BY USER IDENTIFIER ===================== //
    @Override
    public List<Notification> findByUserIdentifier(String userIdentifier) {
        return this.NOTIFICATION_REPOSITORY.findByUserIdentifier(userIdentifier);
    }

    // ================ FIND BY USER IDENTIFIER AND IS READ FALSE ===================== //
    @Override
    public List<Notification> findByUserIdentifierAndIsReadFalse(String userIdentifier) {
        return this.NOTIFICATION_REPOSITORY.findByUserIdentifierAndIsReadFalse(userIdentifier);
    }

    // ================ FIND BY TYPE ===================== //
    @Override
    public List<Notification> findByType(NotificationType type) {
        return this.NOTIFICATION_REPOSITORY.findByType(type);
    }
}
