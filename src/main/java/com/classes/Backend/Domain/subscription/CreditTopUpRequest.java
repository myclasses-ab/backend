package com.classes.Backend.Domain.subscription;

import com.classes.Backend.Domain.enums.CreditTopUpStatus;
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
@Table(name = "credit_top_up_requests")
public class CreditTopUpRequest {

    @Id
    @Column(name = "identifier", unique = true)
    private String identifier = UUID.randomUUID().toString();

    @Column(name = "institute_identifier", nullable = false, length = 36)
    private String instituteIdentifier;

    @Column(name = "requested_credits", nullable = false)
    private Integer requestedCredits;

    @Column(name = "amount_in_rupees", nullable = false)
    private Integer amountInRupees;

    @Column(name = "transaction_id_last6", nullable = false, length = 6)
    private String transactionIdLast6;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private CreditTopUpStatus status = CreditTopUpStatus.PENDING;

    @Column(name = "approved_by", length = 36)
    private String approvedBy;

    @Column(name = "approved_at")
    private LocalDateTime approvedAt;

    @Column(name = "admin_notes", length = 500)
    private String adminNotes;

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
