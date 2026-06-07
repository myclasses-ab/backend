package com.classes.Backend.Service.subscription;

import com.classes.Backend.Domain.subscription.CreditTopUpRequest;

import java.util.List;

public interface CreditTopUpRequestService {
    CreditTopUpRequest createRequest(String instituteIdentifier, Integer requestedCredits, String transactionIdLast6);
    CreditTopUpRequest approveRequest(String identifier, String approvedBy, String adminNotes);
    CreditTopUpRequest rejectRequest(String identifier, String adminNotes);
    List<CreditTopUpRequest> findByInstitute(String instituteIdentifier);
    List<CreditTopUpRequest> findPending();
    List<CreditTopUpRequest> findAll();
    CreditTopUpRequest findById(String identifier);
}
