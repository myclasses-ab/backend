package com.classes.Backend.Repository.leads;

import com.classes.Backend.Domain.enums.LeadDistributionStatus;
import com.classes.Backend.Domain.leads.LeadDistribution;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LeadDistributionRepository extends JpaRepository<LeadDistribution, String> {
    List<LeadDistribution> findByInstituteIdentifier(String instituteIdentifier);
    List<LeadDistribution> findByUserIdentifier(String userIdentifier);
    List<LeadDistribution> findByStatus(LeadDistributionStatus status);
    List<LeadDistribution> findByInstituteIdentifierAndStatus(String instituteIdentifier, LeadDistributionStatus status);
    List<LeadDistribution> findByDistributedBy(String distributedBy);
}
