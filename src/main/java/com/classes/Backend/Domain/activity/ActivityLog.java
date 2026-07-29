package com.classes.Backend.Domain.activity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "activity_logs", indexes = {
        @Index(name = "idx_al_actor", columnList = "actor_type, actor_identifier"),
        @Index(name = "idx_al_entity", columnList = "entity_type, entity_identifier"),
        @Index(name = "idx_al_action", columnList = "action_type"),
        @Index(name = "idx_al_created", columnList = "created_at"),
        @Index(name = "idx_al_institute", columnList = "institute_identifier")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = false)
public class ActivityLog {

    @Id
    @Column(name = "identifier", unique = true)
    @Builder.Default
    private String identifier = UUID.randomUUID().toString();

    @Enumerated(EnumType.STRING)
    @Column(name = "actor_type", nullable = false)
    private ActivityActorType actorType;

    @Column(name = "actor_identifier", length = 100)
    private String actorIdentifier;

    @Column(name = "actor_name", columnDefinition = "TEXT")
    private String actorName;

    @Enumerated(EnumType.STRING)
    @Column(name = "action_type", nullable = false)
    private ActivityActionType actionType;

    @Enumerated(EnumType.STRING)
    @Column(name = "entity_type", nullable = false)
    private ActivityEntityType entityType;

    @Column(name = "entity_identifier", length = 100)
    private String entityIdentifier;

    @Column(name = "entity_name", columnDefinition = "TEXT")
    private String entityName;

    @Column(name = "institute_identifier", length = 100)
    private String instituteIdentifier;

    @Column(name = "description", length = 1000)
    private String description;

    @Column(name = "old_value", columnDefinition = "TEXT")
    private String oldValue;

    @Column(name = "new_value", columnDefinition = "TEXT")
    private String newValue;

    @Column(name = "metadata", columnDefinition = "TEXT")
    private String metadata;

    @Column(name = "ip_address", length = 100)
    private String ipAddress;

    @Column(name = "user_agent", length = 500)
    private String userAgent;

    @Column(name = "source", length = 50)
    private String source;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        if (identifier == null || identifier.isBlank()) {
            identifier = UUID.randomUUID().toString();
        }
        createdAt = LocalDateTime.now();
    }
}
