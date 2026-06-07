package com.classes.Backend.dto.credits;

import com.classes.Backend.Domain.enums.LeadRequestStatus;
import lombok.Data;

@Data
public class UpdateLeadRequestStatus {
    private LeadRequestStatus status;
    private String adminNotes;
}
