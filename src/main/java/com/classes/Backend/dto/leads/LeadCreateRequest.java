package com.classes.Backend.dto.leads;

import com.classes.Backend.Domain.enums.LeadSource;
import lombok.Data;

@Data
public class LeadCreateRequest {
    private String phone;
    private String fullName;
    private String cityIdentifier;
    private String examTypeIdentifier;
    private String searchedQuery;
    private String visitedInstituteIdentifier;
    private String visitedInstituteName;
    private LeadSource source;
}
