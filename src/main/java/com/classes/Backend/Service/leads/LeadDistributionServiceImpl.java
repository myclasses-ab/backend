package com.classes.Backend.Service.leads;

import com.classes.Backend.Domain.enums.LeadDistributionStatus;
import com.classes.Backend.Domain.leads.LeadDistribution;
import com.classes.Backend.Repository.leads.LeadDistributionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class LeadDistributionServiceImpl implements LeadDistributionService {
    private final LeadDistributionRepository LEAD_DISTRIBUTION_REPOSITORY;

    @Override
    public LeadDistribution save(LeadDistribution distribution) {
        return this.LEAD_DISTRIBUTION_REPOSITORY.save(distribution);
    }

    @Override
    public List<LeadDistribution> saveAll(List<LeadDistribution> distributions) {
        return this.LEAD_DISTRIBUTION_REPOSITORY.saveAll(distributions);
    }

    @Override
    public Optional<LeadDistribution> findById(String identifier) {
        return this.LEAD_DISTRIBUTION_REPOSITORY.findById(identifier);
    }

    @Override
    public List<LeadDistribution> findAll() {
        return this.LEAD_DISTRIBUTION_REPOSITORY.findAll();
    }

    @Override
    public List<LeadDistribution> findByInstituteIdentifier(String instituteIdentifier) {
        return this.LEAD_DISTRIBUTION_REPOSITORY.findByInstituteIdentifier(instituteIdentifier);
    }

    @Override
    public List<LeadDistribution> findByUserIdentifier(String userIdentifier) {
        return this.LEAD_DISTRIBUTION_REPOSITORY.findByUserIdentifier(userIdentifier);
    }

    @Override
    public List<LeadDistribution> findByStatus(LeadDistributionStatus status) {
        return this.LEAD_DISTRIBUTION_REPOSITORY.findByStatus(status);
    }

    @Override
    public List<LeadDistribution> findByInstituteIdentifierAndStatus(String instituteIdentifier, LeadDistributionStatus status) {
        return this.LEAD_DISTRIBUTION_REPOSITORY.findByInstituteIdentifierAndStatus(instituteIdentifier, status);
    }

    @Override
    public List<LeadDistribution> findByDistributedBy(String distributedBy) {
        return this.LEAD_DISTRIBUTION_REPOSITORY.findByDistributedBy(distributedBy);
    }

    @Override
    public LeadDistribution updateStatus(String identifier, LeadDistributionStatus status) {
        LeadDistribution distribution = this.LEAD_DISTRIBUTION_REPOSITORY.findById(identifier)
                .orElseThrow(() -> new RuntimeException("LeadDistribution not found with identifier: " + identifier));
        distribution.setStatus(status);
        return this.LEAD_DISTRIBUTION_REPOSITORY.save(distribution);
    }
}
