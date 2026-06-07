package com.classes.Backend.dto.credits;

import lombok.Data;

@Data
public class ApproveCreditTopUpRequest {
    private String approvedBy;
    private String adminNotes;
}
