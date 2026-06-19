package com.classes.Backend.Repository.subscription;

import com.classes.Backend.Domain.enums.CreditTopUpStatus;
import com.classes.Backend.Domain.subscription.CreditTopUpRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface CreditTopUpRequestRepository extends JpaRepository<CreditTopUpRequest, String> {
    List<CreditTopUpRequest> findByInstituteIdentifier(String instituteIdentifier);
    List<CreditTopUpRequest> findByStatus(CreditTopUpStatus status);
    List<CreditTopUpRequest> findByInstituteIdentifierAndStatus(String instituteIdentifier, CreditTopUpStatus status);
    List<CreditTopUpRequest> findByInstituteIdentifierAndTransactionIdLast6AndCreatedAtAfter(String instituteIdentifier, String transactionIdLast6, LocalDateTime createdAtAfter);
}
