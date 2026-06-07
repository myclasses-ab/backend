package com.classes.Backend.dto.credits;

import lombok.Data;

@Data
public class CreateCreditTopUpRequest {
    private String instituteIdentifier;
    private Integer requestedCredits;
    private String transactionIdLast6;
}
