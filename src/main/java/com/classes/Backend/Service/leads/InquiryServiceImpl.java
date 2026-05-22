package com.classes.Backend.Service.leads;

import com.classes.Backend.Domain.leads.Inquiry;
import com.classes.Backend.Domain.enums.InquirySource;
import com.classes.Backend.Domain.enums.InquiryStatus;
import com.classes.Backend.Repository.leads.InquiryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class InquiryServiceImpl implements InquiryService {
    private final InquiryRepository INQUIRY_REPOSITORY;

    // ================ SAVE INQUIRY ===================== //
    @Override
    public Inquiry save(Inquiry inquiry) {
        return this.INQUIRY_REPOSITORY.save(inquiry);
    }

    // ================ SAVE ALL INQUIRIES ===================== //
    @Override
    public List<Inquiry> saveAll(List<Inquiry> inquiries) {
        return this.INQUIRY_REPOSITORY.saveAll(inquiries);
    }

    // ================ FIND BY ID ===================== //
    @Override
    public Optional<Inquiry> findById(String identifier) {
        return this.INQUIRY_REPOSITORY.findById(identifier);
    }

    // ================ FIND ALL ===================== //
    @Override
    public List<Inquiry> findAll() {
        return this.INQUIRY_REPOSITORY.findAll();
    }

    // ================ DELETE BY ID ===================== //
    @Override
    public void deleteById(String identifier) {
        if (!this.INQUIRY_REPOSITORY.existsById(identifier)) {
            throw new RuntimeException("Inquiry with identifier '" + identifier + "' not found");
        }
        this.INQUIRY_REPOSITORY.deleteById(identifier);
    }

    // ================ EXISTS BY ID ===================== //
    @Override
    public boolean existsById(String identifier) {
        return this.INQUIRY_REPOSITORY.existsById(identifier);
    }

    // ================ FIND BY INSTITUTE IDENTIFIER ===================== //
    @Override
    public List<Inquiry> findByInstituteIdentifier(String instituteIdentifier) {
        return this.INQUIRY_REPOSITORY.findByInstituteIdentifier(instituteIdentifier);
    }

    // ================ FIND BY BRANCH IDENTIFIER ===================== //
    @Override
    public List<Inquiry> findByBranchIdentifier(String branchIdentifier) {
        return this.INQUIRY_REPOSITORY.findByBranchIdentifier(branchIdentifier);
    }

    // ================ FIND BY COURSE IDENTIFIER ===================== //
    @Override
    public List<Inquiry> findByCourseIdentifier(String courseIdentifier) {
        return this.INQUIRY_REPOSITORY.findByCourseIdentifier(courseIdentifier);
    }

    // ================ FIND BY USER IDENTIFIER ===================== //
    @Override
    public List<Inquiry> findByUserIdentifier(String userIdentifier) {
        return this.INQUIRY_REPOSITORY.findByUserIdentifier(userIdentifier);
    }

    // ================ FIND BY STATUS ===================== //
    @Override
    public List<Inquiry> findByStatus(InquiryStatus status) {
        return this.INQUIRY_REPOSITORY.findByStatus(status);
    }

    // ================ FIND BY SOURCE ===================== //
    @Override
    public List<Inquiry> findBySource(InquirySource source) {
        return this.INQUIRY_REPOSITORY.findBySource(source);
    }

    // ================ FIND BY ASSIGNED TO ===================== //
    @Override
    public List<Inquiry> findByAssignedTo(String assignedTo) {
        return this.INQUIRY_REPOSITORY.findByAssignedTo(assignedTo);
    }

    // ================ FIND BY INSTITUTE IDENTIFIER AND STATUS ===================== //
    @Override
    public List<Inquiry> findByInstituteIdentifierAndStatus(String instituteIdentifier, InquiryStatus status) {
        return this.INQUIRY_REPOSITORY.findByInstituteIdentifierAndStatus(instituteIdentifier, status);
    }
}
