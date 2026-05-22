package com.classes.Backend.dto.leads;

import lombok.Data;

import java.util.List;

@Data
public class LeadDistributionRequest {
    private List<String> userIdentifiers;
    private String instituteIdentifier;
    private String notes;
}
