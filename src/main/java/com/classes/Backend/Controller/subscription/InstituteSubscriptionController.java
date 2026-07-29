package com.classes.Backend.Controller.subscription;

import com.classes.Backend.Domain.activity.ActivityActionType;
import com.classes.Backend.Domain.activity.ActivityEntityType;
import com.classes.Backend.Domain.subscription.InstituteSubscription;
import com.classes.Backend.Service.activity.ActivityLogActorResolver;
import com.classes.Backend.Service.activity.ActivityLogService;
import com.classes.Backend.Service.activity.ResolvedActor;
import com.classes.Backend.Service.subscription.InstituteSubscriptionServiceImpl;
import com.classes.Backend.dto.activity.ActivityLogRequest;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/institute-subscriptions")
public class InstituteSubscriptionController {

    private final InstituteSubscriptionServiceImpl INSTITUTE_SUBSCRIPTION_SERVICE_IMPL;
    private final ActivityLogService ACTIVITY_LOG_SERVICE;
    private final ActivityLogActorResolver ACTOR_RESOLVER;

    // ================ CREATE INSTITUTE SUBSCRIPTION ===================== //
    @PostMapping
    public ResponseEntity<?> saveInstituteSubscription(@RequestBody InstituteSubscription instituteSubscription, HttpServletRequest request) {
        InstituteSubscription saved = this.INSTITUTE_SUBSCRIPTION_SERVICE_IMPL.save(instituteSubscription);

        ResolvedActor actor = ACTOR_RESOLVER.resolve(request);
        if (actor.isAuthenticated()) {
            ACTIVITY_LOG_SERVICE.log(ActivityLogRequest.builder()
                    .actorType(actor.getType())
                    .actorIdentifier(actor.getIdentifier())
                    .actorName(actor.getName())
                    .actionType(ActivityActionType.SUBSCRIPTION_CHANGED)
                    .entityType(ActivityEntityType.SUBSCRIPTION)
                    .entityIdentifier(saved.getIdentifier())
                    .entityName("Subscription")
                    .instituteIdentifier(saved.getInstituteIdentifier())
                    .description("Created institute subscription" + (saved.getPlanIdentifier() != null ? " on plan " + saved.getPlanIdentifier() : ""))
                    .metadata(Map.of(
                            "planIdentifier", saved.getPlanIdentifier() != null ? saved.getPlanIdentifier() : "",
                            "isActive", saved.getIsActive()
                    ))
                    .source("CONSOLE")
                    .build());
        }

        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }

    // ================ CREATE ALL INSTITUTE SUBSCRIPTIONS ===================== //
    @PostMapping("/bulk")
    public ResponseEntity<?> saveAllInstituteSubscriptions(@RequestBody List<InstituteSubscription> instituteSubscriptions) {
        return new ResponseEntity<>(this.INSTITUTE_SUBSCRIPTION_SERVICE_IMPL.saveAll(instituteSubscriptions), HttpStatus.CREATED);
    }

    // ================ GET INSTITUTE SUBSCRIPTION BY ID ===================== //
    @GetMapping("/{identifier}")
    public ResponseEntity<?> getInstituteSubscriptionById(@PathVariable String identifier) {
        return new ResponseEntity<>(this.INSTITUTE_SUBSCRIPTION_SERVICE_IMPL.findById(identifier), HttpStatus.OK);
    }

    // ================ GET ALL INSTITUTE SUBSCRIPTIONS ===================== //
    @GetMapping
    public ResponseEntity<?> getAllInstituteSubscriptions() {
        List<InstituteSubscription> allSubscriptions = this.INSTITUTE_SUBSCRIPTION_SERVICE_IMPL.findAll();
        return new ResponseEntity<>(allSubscriptions, HttpStatus.OK);
    }

    // ================ DELETE INSTITUTE SUBSCRIPTION BY ID ===================== //
    @DeleteMapping("/{identifier}")
    public ResponseEntity<?> deleteInstituteSubscriptionById(@PathVariable String identifier) {
        this.INSTITUTE_SUBSCRIPTION_SERVICE_IMPL.deleteById(identifier);
        return new ResponseEntity<>("InstituteSubscription deleted successfully", HttpStatus.OK);
    }

    // ================ UPDATE INSTITUTESUBSCRIPTION BY ID ===================== //
    @PutMapping("/{identifier}")
    public ResponseEntity<?> updateInstituteSubscriptionById(@PathVariable String identifier, @RequestBody InstituteSubscription instituteSubscription, HttpServletRequest request) {
        InstituteSubscription existing = this.INSTITUTE_SUBSCRIPTION_SERVICE_IMPL.findById(identifier).orElse(null);
        if (existing == null) {
            return new ResponseEntity<>("InstituteSubscription not found", HttpStatus.NOT_FOUND);
        }
        instituteSubscription.setIdentifier(identifier);
        InstituteSubscription updated = this.INSTITUTE_SUBSCRIPTION_SERVICE_IMPL.save(instituteSubscription);

        ResolvedActor actor = ACTOR_RESOLVER.resolve(request);
        if (actor.isAuthenticated()) {
            ACTIVITY_LOG_SERVICE.log(ActivityLogRequest.builder()
                    .actorType(actor.getType())
                    .actorIdentifier(actor.getIdentifier())
                    .actorName(actor.getName())
                    .actionType(ActivityActionType.SUBSCRIPTION_CHANGED)
                    .entityType(ActivityEntityType.SUBSCRIPTION)
                    .entityIdentifier(updated.getIdentifier())
                    .entityName("Subscription")
                    .instituteIdentifier(updated.getInstituteIdentifier())
                    .description("Updated institute subscription" + (updated.getPlanIdentifier() != null ? " on plan " + updated.getPlanIdentifier() : ""))
                    .metadata(Map.of(
                            "oldPlanIdentifier", existing.getPlanIdentifier() != null ? existing.getPlanIdentifier() : "",
                            "newPlanIdentifier", updated.getPlanIdentifier() != null ? updated.getPlanIdentifier() : "",
                            "isActive", updated.getIsActive()
                    ))
                    .source("CONSOLE")
                    .build());
        }

        return new ResponseEntity<>(updated, HttpStatus.OK);
    }

    // ================ FIND BY INSTITUTE IDENTIFIER ===================== //
    @GetMapping("/institute/{instituteIdentifier}")
    public ResponseEntity<?> findByInstituteIdentifier(@PathVariable String instituteIdentifier) {
        return new ResponseEntity<>(this.INSTITUTE_SUBSCRIPTION_SERVICE_IMPL.findByInstituteIdentifier(instituteIdentifier), HttpStatus.OK);
    }

    // ================ FIND BY PLAN IDENTIFIER ===================== //
    @GetMapping("/plan/{planIdentifier}")
    public ResponseEntity<?> findByPlanIdentifier(@PathVariable String planIdentifier) {
        return new ResponseEntity<>(this.INSTITUTE_SUBSCRIPTION_SERVICE_IMPL.findByPlanIdentifier(planIdentifier), HttpStatus.OK);
    }

    // ================ FIND ACTIVE SUBSCRIPTIONS ===================== //
    @GetMapping("/active")
    public ResponseEntity<?> findByIsActiveTrue() {
        return new ResponseEntity<>(this.INSTITUTE_SUBSCRIPTION_SERVICE_IMPL.findByIsActiveTrue(), HttpStatus.OK);
    }
}
