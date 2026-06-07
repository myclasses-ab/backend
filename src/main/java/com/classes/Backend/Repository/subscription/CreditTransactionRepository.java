package com.classes.Backend.Repository.subscription;

import com.classes.Backend.Domain.subscription.CreditTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CreditTransactionRepository extends JpaRepository<CreditTransaction, String> {
    List<CreditTransaction> findByInstituteIdentifierOrderByCreatedAtDesc(String instituteIdentifier);
}
