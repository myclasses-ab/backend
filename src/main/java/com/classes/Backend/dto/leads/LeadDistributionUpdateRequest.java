package com.classes.Backend.dto.leads;

import com.classes.Backend.Domain.enums.LeadDistributionStatus;
import lombok.Data;

@Data
public class LeadDistributionUpdateRequest {
    private LeadDistributionStatus status;
    private String notes;
    private String instituteNotes;
}
