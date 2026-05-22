package com.classes.Backend.Service.leads;

import com.classes.Backend.Domain.enums.LeadDistributionStatus;
import com.classes.Backend.Domain.leads.LeadDistribution;

import java.util.List;
import java.util.Optional;

public interface LeadDistributionService {
    LeadDistribution save(LeadDistribution distribution);
    List<LeadDistribution> saveAll(List<LeadDistribution> distributions);
    Optional<LeadDistribution> findById(String identifier);
    List<LeadDistribution> findAll();

    List<LeadDistribution> findByInstituteIdentifier(String instituteIdentifier);
    List<LeadDistribution> findByUserIdentifier(String userIdentifier);
    List<LeadDistribution> findByStatus(LeadDistributionStatus status);
    List<LeadDistribution> findByInstituteIdentifierAndStatus(String instituteIdentifier, LeadDistributionStatus status);
    List<LeadDistribution> findByDistributedBy(String distributedBy);

    LeadDistribution updateStatus(String identifier, LeadDistributionStatus status);
}
