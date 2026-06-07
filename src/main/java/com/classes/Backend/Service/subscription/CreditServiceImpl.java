package com.classes.Backend.Service.subscription;

import com.classes.Backend.Domain.enums.CreditTransactionType;
import com.classes.Backend.Domain.subscription.CreditTransaction;
import com.classes.Backend.Domain.subscription.InstituteCredit;
import com.classes.Backend.Repository.institute.InstituteRepository;
import com.classes.Backend.Repository.subscription.CreditTransactionRepository;
import com.classes.Backend.Repository.subscription.InstituteCreditRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class CreditServiceImpl implements CreditService {

    private final InstituteCreditRepository INSTITUTE_CREDIT_REPOSITORY;
    private final CreditTransactionRepository CREDIT_TRANSACTION_REPOSITORY;
    private final InstituteRepository INSTITUTE_REPOSITORY;

    @Override
    public InstituteCredit getOrCreateBalance(String instituteIdentifier) {
        validateInstituteExists(instituteIdentifier);
        return INSTITUTE_CREDIT_REPOSITORY.findByInstituteIdentifier(instituteIdentifier)
                .orElseGet(() -> {
                    InstituteCredit credit = new InstituteCredit();
                    credit.setInstituteIdentifier(instituteIdentifier);
                    credit.setBalance(0);
                    return INSTITUTE_CREDIT_REPOSITORY.save(credit);
                });
    }

    @Override
    @Transactional
    public InstituteCredit grantCredits(String instituteIdentifier, Integer amount, CreditTransactionType type, String description, String referenceId) {
        if (amount == null || amount <= 0) {
            throw new IllegalArgumentException("Amount must be greater than zero");
        }
        InstituteCredit credit = getOrCreateBalance(instituteIdentifier);
        credit.setBalance(credit.getBalance() + amount);
        credit = INSTITUTE_CREDIT_REPOSITORY.save(credit);

        CreditTransaction transaction = new CreditTransaction();
        transaction.setInstituteIdentifier(instituteIdentifier);
        transaction.setAmount(amount);
        transaction.setType(type);
        transaction.setDescription(description);
        transaction.setReferenceIdentifier(referenceId);
        CREDIT_TRANSACTION_REPOSITORY.save(transaction);

        return credit;
    }

    @Override
    @Transactional
    public InstituteCredit deductCredits(String instituteIdentifier, Integer amount, CreditTransactionType type, String description, String referenceId) {
        if (amount == null || amount <= 0) {
            throw new IllegalArgumentException("Amount must be greater than zero");
        }
        InstituteCredit credit = getOrCreateBalance(instituteIdentifier);
        if (credit.getBalance() < amount) {
            throw new IllegalStateException("Insufficient credit balance. Required: " + amount + ", Available: " + credit.getBalance());
        }
        credit.setBalance(credit.getBalance() - amount);
        credit = INSTITUTE_CREDIT_REPOSITORY.save(credit);

        CreditTransaction transaction = new CreditTransaction();
        transaction.setInstituteIdentifier(instituteIdentifier);
        transaction.setAmount(-amount);
        transaction.setType(type);
        transaction.setDescription(description);
        transaction.setReferenceIdentifier(referenceId);
        CREDIT_TRANSACTION_REPOSITORY.save(transaction);

        return credit;
    }

    @Override
    public List<CreditTransaction> getTransactionHistory(String instituteIdentifier) {
        return CREDIT_TRANSACTION_REPOSITORY.findByInstituteIdentifierOrderByCreatedAtDesc(instituteIdentifier);
    }

    @Override
    public boolean hasSufficientBalance(String instituteIdentifier, Integer amount) {
        if (amount == null || amount <= 0) return true;
        return INSTITUTE_CREDIT_REPOSITORY.findByInstituteIdentifier(instituteIdentifier)
                .map(c -> c.getBalance() >= amount)
                .orElse(false);
    }

    private void validateInstituteExists(String instituteIdentifier) {
        if (!INSTITUTE_REPOSITORY.existsById(instituteIdentifier)) {
            throw new IllegalArgumentException("Institute not found with identifier: " + instituteIdentifier);
        }
    }
}
