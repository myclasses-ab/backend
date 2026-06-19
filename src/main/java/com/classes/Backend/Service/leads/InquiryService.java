package com.classes.Backend.Service.leads;

import com.classes.Backend.Domain.leads.Inquiry;
import com.classes.Backend.Domain.enums.InquirySource;
import com.classes.Backend.Domain.enums.InquiryStatus;
import com.classes.Backend.dto.leads.InstituteInquiryResponse;

import java.util.List;
import java.util.Optional;

public interface InquiryService {
    // ================ CRUD OPERATIONS ===================== //
    Inquiry save(Inquiry inquiry);
    List<Inquiry> saveAll(List<Inquiry> inquiries);
    Optional<Inquiry> findById(String identifier);
    List<Inquiry> findAll();
    void deleteById(String identifier);
    boolean existsById(String identifier);

    // ================ CUSTOM FINDER METHODS ===================== //
    List<Inquiry> findByInstituteIdentifier(String instituteIdentifier);
    List<Inquiry> findByBranchIdentifier(String branchIdentifier);
    List<Inquiry> findByCourseIdentifier(String courseIdentifier);
    List<Inquiry> findByUserIdentifier(String userIdentifier);
    List<Inquiry> findByStatus(InquiryStatus status);
    List<Inquiry> findBySource(InquirySource source);
    List<Inquiry> findByAssignedTo(String assignedTo);
    List<Inquiry> findByInstituteIdentifierAndStatus(String instituteIdentifier, InquiryStatus status);

    // ================ INSTITUTE INQUIRY RESPONSE ===================== //
    List<InstituteInquiryResponse> findInstituteInquiryResponses(String instituteIdentifier);
    InstituteInquiryResponse unlockInquiry(String inquiryIdentifier, String instituteIdentifier, String unlockedByUserIdentifier);
}
