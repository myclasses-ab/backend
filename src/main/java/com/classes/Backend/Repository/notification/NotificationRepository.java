package com.classes.Backend.Repository.notification;

import com.classes.Backend.Domain.notification.Notification;
import com.classes.Backend.Domain.enums.NotificationType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, String> {
    List<Notification> findByUserIdentifier(String userIdentifier);
    List<Notification> findByUserIdentifierAndIsReadFalse(String userIdentifier);
    List<Notification> findByType(NotificationType type);
}
