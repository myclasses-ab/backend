package com.classes.Backend.dto.credits;

import lombok.Data;

@Data
public class GrantCreditsRequest {
    private String instituteIdentifier;
    private Integer amount;
    private String description;
}
