package com.classes.Backend.Service.leads;

import com.classes.Backend.Domain.leads.Lead;
import com.classes.Backend.Domain.enums.LeadStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface LeadService {
    Lead save(Lead lead);
    List<Lead> saveAll(List<Lead> leads);
    Optional<Lead> findById(String identifier);
    List<Lead> findAll();
    void deleteById(String identifier);

    List<Lead> findByUserIdentifier(String userIdentifier);
    List<Lead> findByPhone(String phone);
    List<Lead> findByStatus(LeadStatus status);
    List<Lead> findByCityIdentifier(String cityIdentifier);
    List<Lead> findByExamTypeIdentifier(String examTypeIdentifier);
    List<Lead> findByVisitedInstituteIdentifier(String visitedInstituteIdentifier);
    List<Lead> findByCreatedAtBetween(LocalDateTime from, LocalDateTime to);
    List<Lead> findByIsActiveTrue();
    List<Lead> searchByPhoneOrName(String query);

    Lead updateStatus(String identifier, LeadStatus status);
}
