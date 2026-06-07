package com.classes.Backend.Domain.subscription;

import com.classes.Backend.Domain.enums.CreditTransactionType;
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
@Table(name = "credit_transactions")
public class CreditTransaction {

    @Id
    @Column(name = "identifier", unique = true)
    private String identifier = UUID.randomUUID().toString();

    @Column(name = "institute_identifier", nullable = false, length = 36)
    private String instituteIdentifier;

    @Column(name = "amount", nullable = false)
    private Integer amount;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false, length = 50)
    private CreditTransactionType type;

    @Column(name = "description", length = 500)
    private String description;

    @Column(name = "reference_identifier", length = 36)
    private String referenceIdentifier;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}
