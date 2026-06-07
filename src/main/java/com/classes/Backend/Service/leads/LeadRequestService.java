package com.classes.Backend.Service.leads;

import com.classes.Backend.Domain.leads.LeadRequest;

import java.util.List;

public interface LeadRequestService {
    LeadRequest createRequest(String instituteIdentifier, String examTypeIdentifier, Integer quantity, String notes);
    LeadRequest approveRequest(String identifier, String adminNotes);
    LeadRequest rejectRequest(String identifier, String adminNotes);
    LeadRequest fulfillRequest(String identifier);
    LeadRequest cancelRequest(String identifier);
    List<LeadRequest> findByInstitute(String instituteIdentifier);
    List<LeadRequest> findPending();
    List<LeadRequest> findAll();
    LeadRequest findById(String identifier);
}
