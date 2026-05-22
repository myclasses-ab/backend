package com.classes.Backend.Service.leads;

import com.classes.Backend.Domain.enums.LeadStatus;
import com.classes.Backend.Domain.leads.Lead;
import com.classes.Backend.Repository.leads.LeadRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class LeadServiceImpl implements LeadService {
    private final LeadRepository LEAD_REPOSITORY;

    @Override
    public Lead save(Lead lead) {
        return this.LEAD_REPOSITORY.save(lead);
    }

    @Override
    public List<Lead> saveAll(List<Lead> leads) {
        return this.LEAD_REPOSITORY.saveAll(leads);
    }

    @Override
    public Optional<Lead> findById(String identifier) {
        return this.LEAD_REPOSITORY.findById(identifier);
    }

    @Override
    public List<Lead> findAll() {
        return this.LEAD_REPOSITORY.findAll();
    }

    @Override
    public void deleteById(String identifier) {
        this.LEAD_REPOSITORY.deleteById(identifier);
    }

    @Override
    public List<Lead> findByUserIdentifier(String userIdentifier) {
        return this.LEAD_REPOSITORY.findByUserIdentifier(userIdentifier);
    }

    @Override
    public List<Lead> findByPhone(String phone) {
        return this.LEAD_REPOSITORY.findByPhone(phone);
    }

    @Override
    public List<Lead> findByStatus(LeadStatus status) {
        return this.LEAD_REPOSITORY.findByStatus(status);
    }

    @Override
    public List<Lead> findByCityIdentifier(String cityIdentifier) {
        return this.LEAD_REPOSITORY.findByCityIdentifier(cityIdentifier);
    }

    @Override
    public List<Lead> findByExamTypeIdentifier(String examTypeIdentifier) {
        return this.LEAD_REPOSITORY.findByExamTypeIdentifier(examTypeIdentifier);
    }

    @Override
    public List<Lead> findByVisitedInstituteIdentifier(String visitedInstituteIdentifier) {
        return this.LEAD_REPOSITORY.findByVisitedInstituteIdentifier(visitedInstituteIdentifier);
    }

    @Override
    public List<Lead> findByCreatedAtBetween(LocalDateTime from, LocalDateTime to) {
        return this.LEAD_REPOSITORY.findByCreatedAtBetween(from, to);
    }

    @Override
    public List<Lead> findByIsActiveTrue() {
        return this.LEAD_REPOSITORY.findByIsActiveTrue();
    }

    @Override
    public List<Lead> searchByPhoneOrName(String query) {
        return this.LEAD_REPOSITORY.findByPhoneContainingOrFullNameContainingIgnoreCase(query, query);
    }

    @Override
    public Lead updateStatus(String identifier, LeadStatus status) {
        Lead lead = this.LEAD_REPOSITORY.findById(identifier)
                .orElseThrow(() -> new RuntimeException("Lead not found with identifier: " + identifier));
        lead.setStatus(status);
        return this.LEAD_REPOSITORY.save(lead);
    }
}
