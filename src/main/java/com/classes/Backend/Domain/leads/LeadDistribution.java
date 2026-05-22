package com.classes.Backend.Domain.leads;

import com.classes.Backend.Domain.enums.LeadDistributionStatus;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Table(name = "lead_distributions")
public class LeadDistribution {

    @Id
    @Column(name = "identifier", unique = true)
    private String identifier = UUID.randomUUID().toString();

    @Column(name = "user_identifier", nullable = false)
    private String userIdentifier;

    @Column(name = "user_name", length = 200)
    private String userName;

    @Column(name = "user_phone", length = 20)
    private String userPhone;

    @Column(name = "institute_identifier", nullable = false)
    private String instituteIdentifier;

    @Column(name = "institute_name", length = 300)
    private String instituteName;

    @Column(name = "distributed_by")
    private String distributedBy;

    @Column(name = "distributed_at")
    private LocalDateTime distributedAt;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private LeadDistributionStatus status = LeadDistributionStatus.PENDING;

    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;

    @Column(name = "institute_notes", columnDefinition = "TEXT")
    private String instituteNotes;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        if (distributedAt == null) {
            distributedAt = LocalDateTime.now();
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
