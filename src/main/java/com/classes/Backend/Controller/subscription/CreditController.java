package com.classes.Backend.Controller.subscription;

import com.classes.Backend.Domain.activity.ActivityActionType;
import com.classes.Backend.Domain.activity.ActivityEntityType;
import com.classes.Backend.Service.activity.ActivityLogActorResolver;
import com.classes.Backend.Service.activity.ActivityLogService;
import com.classes.Backend.Service.activity.ResolvedActor;
import com.classes.Backend.Service.subscription.CreditServiceImpl;
import com.classes.Backend.dto.activity.ActivityLogRequest;
import com.classes.Backend.dto.credits.GrantCreditsRequest;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/credits")
public class CreditController {

    private final CreditServiceImpl CREDIT_SERVICE_IMPL;
    private final ActivityLogService ACTIVITY_LOG_SERVICE;
    private final ActivityLogActorResolver ACTOR_RESOLVER;

    @PostMapping("/grant")
    public ResponseEntity<?> grantCredits(@RequestBody GrantCreditsRequest request, HttpServletRequest httpRequest) {
        try {
            var credit = CREDIT_SERVICE_IMPL.grantCredits(
                    request.getInstituteIdentifier(),
                    request.getAmount(),
                    com.classes.Backend.Domain.enums.CreditTransactionType.GRANTED,
                    request.getDescription(),
                    null
            );

            ResolvedActor actor = ACTOR_RESOLVER.resolve(httpRequest);
            if (actor.isAuthenticated()) {
                ACTIVITY_LOG_SERVICE.log(ActivityLogRequest.builder()
                        .actorType(actor.getType())
                        .actorIdentifier(actor.getIdentifier())
                        .actorName(actor.getName())
                        .actionType(ActivityActionType.CREDITS_GRANTED)
                        .entityType(ActivityEntityType.CREDIT)
                        .entityIdentifier(credit.getIdentifier())
                        .entityName("Credit Balance")
                        .instituteIdentifier(request.getInstituteIdentifier())
                        .description("Granted " + request.getAmount() + " credits" + (request.getDescription() != null ? ": " + request.getDescription() : ""))
                        .metadata(Map.of(
                                "amount", request.getAmount(),
                                "description", request.getDescription() != null ? request.getDescription() : ""
                        ))
                        .source("SUPER_ADMIN")
                        .build());
            }

            return new ResponseEntity<>(credit, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
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
