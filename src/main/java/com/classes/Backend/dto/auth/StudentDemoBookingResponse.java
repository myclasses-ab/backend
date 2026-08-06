package com.classes.Backend.dto.auth;

import com.classes.Backend.Domain.enums.InquiryStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StudentDemoBookingResponse {

    private String identifier;
    private String instituteIdentifier;
    private String instituteName;
    private String courseIdentifier;
    private String courseName;
    private InquiryStatus status;
    private LocalDateTime createdAt;
}
