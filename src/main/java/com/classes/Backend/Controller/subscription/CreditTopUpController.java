package com.classes.Backend.Controller.subscription;

import com.classes.Backend.dto.credits.ApproveCreditTopUpRequest;
import com.classes.Backend.dto.credits.CreateCreditTopUpRequest;
import com.classes.Backend.Service.subscription.CreditTopUpRequestServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/credit-top-ups")
public class CreditTopUpController {

    private final CreditTopUpRequestServiceImpl CREDIT_TOP_UP_REQUEST_SERVICE_IMPL;

    @PostMapping
    public ResponseEntity<?> createCreditTopUp(@RequestBody CreateCreditTopUpRequest request) {
        try {
            var topUp = CREDIT_TOP_UP_REQUEST_SERVICE_IMPL.createRequest(
                    request.getInstituteIdentifier(),
                    request.getRequestedCredits(),
                    request.getTransactionIdLast6()
            );
            return new ResponseEntity<>(topUp, HttpStatus.CREATED);
        } catch (IllegalArgumentException | IllegalStateException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping
    public ResponseEntity<?> getAllCreditTopUps() {
        return ResponseEntity.ok(CREDIT_TOP_UP_REQUEST_SERVICE_IMPL.findAll());
    }

    @GetMapping("/pending")
    public ResponseEntity<?> getPendingCreditTopUps() {
        return ResponseEntity.ok(CREDIT_TOP_UP_REQUEST_SERVICE_IMPL.findPending());
    }

    @GetMapping("/institute/{instituteIdentifier}")
    public ResponseEntity<?> getCreditTopUpsByInstitute(@PathVariable String instituteIdentifier) {
        return ResponseEntity.ok(CREDIT_TOP_UP_REQUEST_SERVICE_IMPL.findByInstitute(instituteIdentifier));
    }

    @PutMapping("/{identifier}/approve")
    public ResponseEntity<?> approveCreditTopUp(@PathVariable String identifier, @RequestBody ApproveCreditTopUpRequest request) {
        try {
            var updated = CREDIT_TOP_UP_REQUEST_SERVICE_IMPL.approveRequest(
                    identifier,
                    request.getApprovedBy(),
                    request.getAdminNotes()
            );
            return ResponseEntity.ok(updated);
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PutMapping("/{identifier}/reject")
    public ResponseEntity<?> rejectCreditTopUp(@PathVariable String identifier, @RequestBody Map<String, String> request) {
        try {
            var updated = CREDIT_TOP_UP_REQUEST_SERVICE_IMPL.rejectRequest(
                    identifier,
                    request.get("adminNotes")
            );
            return ResponseEntity.ok(updated);
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}
