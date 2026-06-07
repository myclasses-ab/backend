package com.classes.Backend.dto.credits;

import lombok.Data;

@Data
public class CreateLeadRequest {
    private String instituteIdentifier;
    private String examTypeIdentifier;
    private Integer quantity;
    private String notes;
}
