package com.classes.Backend.Domain.leads;

import com.classes.Backend.Domain.enums.InquirySource;
import com.classes.Backend.Domain.enums.InquiryStatus;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Table(name = "inquiries")
public class Inquiry {

    @Id
    @Column(name = "identifier", unique = true)
    private String identifier = UUID.randomUUID().toString();

    @Column(name = "institute_identifier")
    private String instituteIdentifier;

    @Column(name = "branch_identifier")
    private String branchIdentifier;

    @Column(name = "course_identifier")
    private String courseIdentifier;

    @Column(name = "course_fee", precision = 12, scale = 2)
    private BigDecimal courseFee;

    @Column(name = "user_identifier")
    private String userIdentifier;

    @Column(name = "name", length = 200)
    private String name;

    @Column(name = "email", length = 255)
    private String email;

    @Column(name = "phone", length = 20)
    private String phone;

    @Column(name = "standard", length = 10)
    private String standard;

    @Column(name = "target_exam", length = 200)
    private String targetExam;

    @Column(name = "message", columnDefinition = "TEXT")
    private String message;

    @Enumerated(EnumType.STRING)
    @Column(name = "source")
    private InquirySource source;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private InquiryStatus status = InquiryStatus.NEW;

    @Column(name = "assigned_to")
    private String assignedTo;

    @Column(name = "institute_notes", columnDefinition = "TEXT")
    private String instituteNotes;

    @Column(name = "utm_source", length = 200)
    private String utmSource;

    @Column(name = "utm_medium", length = 200)
    private String utmMedium;

    @Column(name = "utm_campaign", length = 200)
    private String utmCampaign;

    @Column(name = "contact_unlocked")
    private Boolean contactUnlocked = false;

    @Column(name = "unlocked_at")
    private LocalDateTime unlockedAt;

    @Column(name = "unlocked_by")
    private String unlockedBy;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
