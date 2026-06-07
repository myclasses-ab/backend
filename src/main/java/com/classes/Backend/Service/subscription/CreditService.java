package com.classes.Backend.Service.subscription;

import com.classes.Backend.Domain.enums.CreditTransactionType;
import com.classes.Backend.Domain.subscription.CreditTransaction;
import com.classes.Backend.Domain.subscription.InstituteCredit;

import java.util.List;

public interface CreditService {
    InstituteCredit getOrCreateBalance(String instituteIdentifier);
    InstituteCredit grantCredits(String instituteIdentifier, Integer amount, CreditTransactionType type, String description, String referenceId);
    InstituteCredit deductCredits(String instituteIdentifier, Integer amount, CreditTransactionType type, String description, String referenceId);
    List<CreditTransaction> getTransactionHistory(String instituteIdentifier);
    boolean hasSufficientBalance(String instituteIdentifier, Integer amount);
}
