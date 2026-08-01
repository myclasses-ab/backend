package com.classes.Backend.dto.leads;

import com.classes.Backend.Domain.enums.InquiryStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InquiryUpdateRequest {
    private InquiryStatus status;
    private String instituteNotes;
    private String assignedTo;
    private Boolean contactUnlocked;
}
