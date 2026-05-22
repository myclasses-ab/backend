package com.classes.Backend.Repository.leads;

import com.classes.Backend.Domain.leads.Inquiry;
import com.classes.Backend.Domain.enums.InquirySource;
import com.classes.Backend.Domain.enums.InquiryStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InquiryRepository extends JpaRepository<Inquiry, String> {
    List<Inquiry> findByInstituteIdentifier(String instituteIdentifier);
    List<Inquiry> findByBranchIdentifier(String branchIdentifier);
    List<Inquiry> findByCourseIdentifier(String courseIdentifier);
    List<Inquiry> findByUserIdentifier(String userIdentifier);
    List<Inquiry> findByStatus(InquiryStatus status);
    List<Inquiry> findBySource(InquirySource source);
    List<Inquiry> findByAssignedTo(String assignedTo);
    List<Inquiry> findByInstituteIdentifierAndStatus(String instituteIdentifier, InquiryStatus status);
}
