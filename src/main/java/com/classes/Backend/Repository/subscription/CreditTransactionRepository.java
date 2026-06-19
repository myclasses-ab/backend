package com.classes.Backend.Repository.subscription;

import com.classes.Backend.Domain.subscription.CreditTransaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CreditTransactionRepository extends JpaRepository<CreditTransaction, String> {
    List<CreditTransaction> findByInstituteIdentifierOrderByCreatedAtDesc(String instituteIdentifier);
}
