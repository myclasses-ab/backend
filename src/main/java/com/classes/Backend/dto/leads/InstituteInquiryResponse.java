package com.classes.Backend.dto.leads;

import com.classes.Backend.Domain.enums.InquirySource;
import com.classes.Backend.Domain.enums.InquiryStatus;
import com.classes.Backend.Domain.leads.Inquiry;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InstituteInquiryResponse {

    private String identifier;
    private String instituteIdentifier;
    private String branchIdentifier;
    private String courseIdentifier;
    private String courseName;
    private String studentName;
    private String studentPhone;
    private String email;
    private String standard;
    private String targetExam;
    private String message;
    private InquirySource source;
    private InquiryStatus status;
    private String instituteNotes;
    private Boolean contactUnlocked;
    private LocalDateTime unlockedAt;
    private String unlockedBy;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static InstituteInquiryResponse fromMasked(Inquiry inquiry, String courseName) {
        return InstituteInquiryResponse.builder()
                .identifier(inquiry.getIdentifier())
                .instituteIdentifier(inquiry.getInstituteIdentifier())
                .branchIdentifier(inquiry.getBranchIdentifier())
                .courseIdentifier(inquiry.getCourseIdentifier())
                .courseName(courseName)
                .studentName(maskName(inquiry.getName()))
                .studentPhone(maskPhone(inquiry.getPhone()))
                .email(inquiry.getEmail())
                .standard(inquiry.getStandard())
                .targetExam(inquiry.getTargetExam())
                .message(inquiry.getMessage())
                .source(inquiry.getSource())
                .status(inquiry.getStatus())
                .instituteNotes(inquiry.getInstituteNotes())
                .contactUnlocked(inquiry.getContactUnlocked())
                .unlockedAt(inquiry.getUnlockedAt())
                .unlockedBy(inquiry.getUnlockedBy())
                .createdAt(inquiry.getCreatedAt())
                .updatedAt(inquiry.getUpdatedAt())
                .build();
    }

    public static InstituteInquiryResponse fromUnmasked(Inquiry inquiry, String courseName) {
        return InstituteInquiryResponse.builder()
                .identifier(inquiry.getIdentifier())
                .instituteIdentifier(inquiry.getInstituteIdentifier())
                .branchIdentifier(inquiry.getBranchIdentifier())
                .courseIdentifier(inquiry.getCourseIdentifier())
                .courseName(courseName)
                .studentName(inquiry.getName())
                .studentPhone(inquiry.getPhone())
                .email(inquiry.getEmail())
                .standard(inquiry.getStandard())
                .targetExam(inquiry.getTargetExam())
                .message(inquiry.getMessage())
                .source(inquiry.getSource())
                .status(inquiry.getStatus())
                .instituteNotes(inquiry.getInstituteNotes())
                .contactUnlocked(inquiry.getContactUnlocked())
                .unlockedAt(inquiry.getUnlockedAt())
                .unlockedBy(inquiry.getUnlockedBy())
                .createdAt(inquiry.getCreatedAt())
                .updatedAt(inquiry.getUpdatedAt())
                .build();
    }

    private static String maskName(String name) {
        if (name == null || name.isBlank()) {
            return null;
        }
        if (name.length() < 3) {
            return name.charAt(0) + "***";
        }
        return name.charAt(0) + "***" + name.charAt(name.length() - 1);
    }

    private static String maskPhone(String phone) {
        if (phone == null || phone.length() < 5) {
            return phone;
        }
        return phone.substring(0, 2) + "******" + phone.substring(phone.length() - 2);
    }
}
