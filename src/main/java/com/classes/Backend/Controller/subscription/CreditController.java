package com.classes.Backend.Controller.subscription;

import com.classes.Backend.dto.credits.GrantCreditsRequest;
import com.classes.Backend.Service.subscription.CreditServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/credits")
public class CreditController {

    private final CreditServiceImpl CREDIT_SERVICE_IMPL;

    @PostMapping("/grant")
    public ResponseEntity<?> grantCredits(@RequestBody GrantCreditsRequest request) {
        try {
            var credit = CREDIT_SERVICE_IMPL.grantCredits(
                    request.getInstituteIdentifier(),
                    request.getAmount(),
                    com.classes.Backend.Domain.enums.CreditTransactionType.GRANTED,
                    request.getDescription(),
                    null
            );
            return new ResponseEntity<>(credit, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(java.util.Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/institute/{instituteIdentifier}")
    public ResponseEntity<?> getBalance(@PathVariable String instituteIdentifier) {
        try {
            var credit = CREDIT_SERVICE_IMPL.getOrCreateBalance(instituteIdentifier);
            return ResponseEntity.ok(credit);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(java.util.Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/institute/{instituteIdentifier}/transactions")
    public ResponseEntity<?> getTransactions(@PathVariable String instituteIdentifier) {
        return ResponseEntity.ok(CREDIT_SERVICE_IMPL.getTransactionHistory(instituteIdentifier));
    }
}
