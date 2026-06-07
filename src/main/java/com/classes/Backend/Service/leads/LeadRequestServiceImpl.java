package com.classes.Backend.Service.leads;

import com.classes.Backend.Domain.enums.CreditTransactionType;
import com.classes.Backend.Domain.enums.LeadRequestStatus;
import com.classes.Backend.Domain.leads.LeadRequest;
import com.classes.Backend.Repository.leads.LeadRequestRepository;
import com.classes.Backend.Service.subscription.CreditServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class LeadRequestServiceImpl implements LeadRequestService {

    private final LeadRequestRepository LEAD_REQUEST_REPOSITORY;
    private final CreditServiceImpl CREDIT_SERVICE_IMPL;

    @Value("${credits.lead-cost-per-lead:1}")
    private Integer leadCostPerLead;

    @Override
    @Transactional
    public LeadRequest createRequest(String instituteIdentifier, String examTypeIdentifier, Integer quantity, String notes) {
        if (quantity == null || quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than zero");
        }
        int totalCost = quantity * leadCostPerLead;

        if (!CREDIT_SERVICE_IMPL.hasSufficientBalance(instituteIdentifier, totalCost)) {
            throw new IllegalStateException("Insufficient credit balance for this request");
        }

        CREDIT_SERVICE_IMPL.deductCredits(
                instituteIdentifier,
                totalCost,
                CreditTransactionType.DEDUCTED_FOR_LEADS,
                "Deducted for lead request: " + quantity + " leads",
                null
        );

        LeadRequest request = new LeadRequest();
        request.setInstituteIdentifier(instituteIdentifier);
        request.setExamTypeIdentifier(examTypeIdentifier);
        request.setQuantity(quantity);
        request.setTotalCost(totalCost);
        request.setStatus(LeadRequestStatus.PENDING);
        request.setNotes(notes);

        return LEAD_REQUEST_REPOSITORY.save(request);
    }

    @Override
    @Transactional
    public LeadRequest approveRequest(String identifier, String adminNotes) {
        LeadRequest request = findById(identifier);
        if (request.getStatus() != LeadRequestStatus.PENDING) {
            throw new IllegalStateException("Only PENDING requests can be approved");
        }
        request.setStatus(LeadRequestStatus.APPROVED);
        request.setAdminNotes(adminNotes);
        return LEAD_REQUEST_REPOSITORY.save(request);
    }

    @Override
    @Transactional
    public LeadRequest rejectRequest(String identifier, String adminNotes) {
        LeadRequest request = findById(identifier);
        if (request.getStatus() != LeadRequestStatus.PENDING) {
            throw new IllegalStateException("Only PENDING requests can be rejected");
        }
        CREDIT_SERVICE_IMPL.grantCredits(
                request.getInstituteIdentifier(),
                request.getTotalCost(),
                CreditTransactionType.REFUNDED,
                "Refunded for rejected lead request",
                request.getIdentifier()
        );
        request.setStatus(LeadRequestStatus.REJECTED);
        request.setAdminNotes(adminNotes);
        return LEAD_REQUEST_REPOSITORY.save(request);
    }

    @Override
    @Transactional
    public LeadRequest fulfillRequest(String identifier) {
        LeadRequest request = findById(identifier);
        if (request.getStatus() != LeadRequestStatus.APPROVED) {
            throw new IllegalStateException("Only APPROVED requests can be fulfilled");
        }
        request.setStatus(LeadRequestStatus.FULFILLED);
        return LEAD_REQUEST_REPOSITORY.save(request);
    }

    @Override
    @Transactional
    public LeadRequest cancelRequest(String identifier) {
        LeadRequest request = findById(identifier);
        if (request.getStatus() != LeadRequestStatus.PENDING) {
            throw new IllegalStateException("Only PENDING requests can be cancelled");
        }
        CREDIT_SERVICE_IMPL.grantCredits(
                request.getInstituteIdentifier(),
                request.getTotalCost(),
                CreditTransactionType.REFUNDED,
                "Refunded for cancelled lead request",
                request.getIdentifier()
        );
        request.setStatus(LeadRequestStatus.CANCELLED);
        return LEAD_REQUEST_REPOSITORY.save(request);
    }

    @Override
    public List<LeadRequest> findByInstitute(String instituteIdentifier) {
        return LEAD_REQUEST_REPOSITORY.findByInstituteIdentifier(instituteIdentifier);
    }

    @Override
    public List<LeadRequest> findPending() {
        return LEAD_REQUEST_REPOSITORY.findByStatus(LeadRequestStatus.PENDING);
    }

    @Override
    public List<LeadRequest> findAll() {
        return LEAD_REQUEST_REPOSITORY.findAll();
    }

    @Override
    public LeadRequest findById(String identifier) {
        return LEAD_REQUEST_REPOSITORY.findById(identifier)
                .orElseThrow(() -> new RuntimeException("LeadRequest not found with identifier: " + identifier));
    }
}
