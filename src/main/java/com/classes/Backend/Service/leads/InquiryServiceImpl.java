package com.classes.Backend.Service.leads;

import com.classes.Backend.Domain.course.InstituteCourse;
import com.classes.Backend.Domain.leads.Inquiry;
import com.classes.Backend.Domain.enums.CreditTransactionType;
import com.classes.Backend.Domain.enums.InquirySource;
import com.classes.Backend.Domain.enums.InquiryStatus;
import com.classes.Backend.Repository.course.InstituteCourseRepository;
import com.classes.Backend.Repository.leads.InquiryRepository;
import com.classes.Backend.Service.subscription.CreditServiceImpl;
import com.classes.Backend.dto.leads.InstituteInquiryResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class InquiryServiceImpl implements InquiryService {
    private final InquiryRepository INQUIRY_REPOSITORY;
    private final InstituteCourseRepository INSTITUTE_COURSE_REPOSITORY;
    private final CreditServiceImpl CREDIT_SERVICE_IMPL;

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

    // ================ FIND INSTITUTE INQUIRY RESPONSES ===================== //
    @Override
    public List<InstituteInquiryResponse> findInstituteInquiryResponses(String instituteIdentifier) {
        List<Inquiry> inquiries = this.INQUIRY_REPOSITORY.findByInstituteIdentifier(instituteIdentifier);
        Map<String, String> courseNames = loadCourseNames(instituteIdentifier);
        return inquiries.stream()
                .map(inquiry -> {
                    String courseName = courseNames.getOrDefault(inquiry.getCourseIdentifier(), null);
                    Boolean unlocked = inquiry.getContactUnlocked();
                    if (unlocked != null && unlocked) {
                        return InstituteInquiryResponse.fromUnmasked(inquiry, courseName);
                    }
                    return InstituteInquiryResponse.fromMasked(inquiry, courseName);
                })
                .collect(Collectors.toList());
    }

    // ================ UNLOCK INQUIRY ===================== //
    @Override
    @Transactional
    public InstituteInquiryResponse unlockInquiry(String inquiryIdentifier, String instituteIdentifier, String unlockedByUserIdentifier) {
        Inquiry inquiry = this.INQUIRY_REPOSITORY.findById(inquiryIdentifier)
                .orElseThrow(() -> new RuntimeException("Inquiry not found with identifier: " + inquiryIdentifier));

        if (!instituteIdentifier.equals(inquiry.getInstituteIdentifier())) {
            throw new IllegalArgumentException("Inquiry does not belong to the specified institute");
        }

        Boolean unlocked = inquiry.getContactUnlocked();
        if (unlocked != null && unlocked) {
            throw new IllegalStateException("Inquiry is already unlocked");
        }

        CREDIT_SERVICE_IMPL.deductCredits(
                instituteIdentifier,
                1,
                CreditTransactionType.DEDUCTED_FOR_LEAD_UNLOCK,
                "Unlocked contact for inquiry " + inquiryIdentifier,
                inquiryIdentifier
        );

        inquiry.setContactUnlocked(true);
        inquiry.setUnlockedAt(LocalDateTime.now());
        inquiry.setUnlockedBy(unlockedByUserIdentifier);
        Inquiry saved = this.INQUIRY_REPOSITORY.save(inquiry);

        String courseName = loadCourseNames(instituteIdentifier).getOrDefault(saved.getCourseIdentifier(), null);
        return InstituteInquiryResponse.fromUnmasked(saved, courseName);
    }

    // ================ HELPER METHODS ===================== //
    private Map<String, String> loadCourseNames(String instituteIdentifier) {
        return this.INSTITUTE_COURSE_REPOSITORY.findByInstituteIdentifier(instituteIdentifier).stream()
                .filter(course -> course.getCourseName() != null)
                .collect(Collectors.toMap(InstituteCourse::getIdentifier, InstituteCourse::getCourseName, (a, b) -> a));
    }
}
