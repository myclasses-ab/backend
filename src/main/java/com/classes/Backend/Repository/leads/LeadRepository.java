package com.classes.Backend.Repository.leads;

import com.classes.Backend.Domain.enums.LeadStatus;
import com.classes.Backend.Domain.leads.Lead;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface LeadRepository extends JpaRepository<Lead, String> {
    List<Lead> findByUserIdentifier(String userIdentifier);
    List<Lead> findByPhone(String phone);
    List<Lead> findByStatus(LeadStatus status);
    List<Lead> findByCityIdentifier(String cityIdentifier);
    List<Lead> findByExamTypeIdentifier(String examTypeIdentifier);
    List<Lead> findByVisitedInstituteIdentifier(String visitedInstituteIdentifier);
    List<Lead> findByCreatedAtBetween(LocalDateTime from, LocalDateTime to);
    List<Lead> findByIsActiveTrue();
    List<Lead> findByPhoneContainingOrFullNameContainingIgnoreCase(String phone, String fullName);
}
