package com.classes.Backend.Repository.leads;

import com.classes.Backend.Domain.enums.LeadRequestStatus;
import com.classes.Backend.Domain.leads.LeadRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LeadRequestRepository extends JpaRepository<LeadRequest, String> {
    List<LeadRequest> findByInstituteIdentifier(String instituteIdentifier);
    List<LeadRequest> findByStatus(LeadRequestStatus status);
    List<LeadRequest> findByInstituteIdentifierAndStatus(String instituteIdentifier, LeadRequestStatus status);
}
