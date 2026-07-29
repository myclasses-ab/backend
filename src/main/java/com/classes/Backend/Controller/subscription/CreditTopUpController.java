package com.classes.Backend.Controller.subscription;

import com.classes.Backend.Domain.activity.ActivityActionType;
import com.classes.Backend.Domain.activity.ActivityEntityType;
import com.classes.Backend.Domain.subscription.CreditTopUpRequest;
import com.classes.Backend.Service.activity.ActivityLogActorResolver;
import com.classes.Backend.Service.activity.ActivityLogService;
import com.classes.Backend.Service.activity.ResolvedActor;
import com.classes.Backend.Service.subscription.CreditTopUpRequestServiceImpl;
import com.classes.Backend.dto.activity.ActivityLogRequest;
import com.classes.Backend.dto.credits.ApproveCreditTopUpRequest;
import com.classes.Backend.dto.credits.CreateCreditTopUpRequest;
import jakarta.servlet.http.HttpServletRequest;
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
    private final ActivityLogService ACTIVITY_LOG_SERVICE;
    private final ActivityLogActorResolver ACTOR_RESOLVER;

    @PostMapping
    public ResponseEntity<?> createCreditTopUp(@RequestBody CreateCreditTopUpRequest request, HttpServletRequest httpRequest) {
        try {
            var topUp = CREDIT_TOP_UP_REQUEST_SERVICE_IMPL.createRequest(
                    request.getInstituteIdentifier(),
                    request.getRequestedCredits(),
                    request.getTransactionIdLast6()
            );

            logTopUpEvent(topUp, ActivityActionType.TOP_UP_REQUESTED, "Requested credit top-up", httpRequest, "CONSOLE");

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
    public ResponseEntity<?> approveCreditTopUp(@PathVariable String identifier, @RequestBody ApproveCreditTopUpRequest request, HttpServletRequest httpRequest) {
        try {
            var updated = CREDIT_TOP_UP_REQUEST_SERVICE_IMPL.approveRequest(
                    identifier,
                    request.getApprovedBy(),
                    request.getAdminNotes()
            );

            logTopUpEvent(updated, ActivityActionType.TOP_UP_APPROVED, "Approved credit top-up", httpRequest, "SUPER_ADMIN");

            return ResponseEntity.ok(updated);
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PutMapping("/{identifier}/reject")
    public ResponseEntity<?> rejectCreditTopUp(@PathVariable String identifier, @RequestBody Map<String, String> request, HttpServletRequest httpRequest) {
        try {
            var updated = CREDIT_TOP_UP_REQUEST_SERVICE_IMPL.rejectRequest(
                    identifier,
                    request.get("adminNotes")
            );

            logTopUpEvent(updated, ActivityActionType.TOP_UP_REJECTED, "Rejected credit top-up", httpRequest, "SUPER_ADMIN");

            return ResponseEntity.ok(updated);
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    private void logTopUpEvent(CreditTopUpRequest topUp, ActivityActionType actionType, String description, HttpServletRequest request, String source) {
        ResolvedActor actor = ACTOR_RESOLVER.resolve(request);
        if (actor.isAuthenticated()) {
            ACTIVITY_LOG_SERVICE.log(ActivityLogRequest.builder()
                    .actorType(actor.getType())
                    .actorIdentifier(actor.getIdentifier())
                    .actorName(actor.getName())
                    .actionType(actionType)
                    .entityType(ActivityEntityType.CREDIT_TRANSACTION)
                    .entityIdentifier(topUp.getIdentifier())
                    .entityName("Top-up " + topUp.getRequestedCredits() + " credits")
                    .instituteIdentifier(topUp.getInstituteIdentifier())
                    .description(description + " for " + topUp.getRequestedCredits() + " credits")
                    .metadata(Map.of(
                            "requestedCredits", topUp.getRequestedCredits(),
                            "amountInRupees", topUp.getAmountInRupees(),
                            "transactionIdLast6", topUp.getTransactionIdLast6() != null ? topUp.getTransactionIdLast6() : "",
                            "status", topUp.getStatus() != null ? topUp.getStatus().name() : ""
                    ))
                    .source(source)
                    .build());
        }
    }
}
