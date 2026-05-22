package com.classes.Backend.Controller.subscription;

import com.classes.Backend.Domain.enums.SubscriptionTier;
import com.classes.Backend.Domain.subscription.SubscriptionPlan;
import com.classes.Backend.Service.subscription.SubscriptionPlanServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/subscription-plans")
public class SubscriptionPlanController {

    private final SubscriptionPlanServiceImpl SUBSCRIPTION_PLAN_SERVICE_IMPL;

    // ================ CREATE SUBSCRIPTION PLAN ===================== //
    @PostMapping
    public ResponseEntity<?> saveSubscriptionPlan(@RequestBody SubscriptionPlan subscriptionPlan) {
        return new ResponseEntity<>(this.SUBSCRIPTION_PLAN_SERVICE_IMPL.save(subscriptionPlan), HttpStatus.CREATED);
    }

    // ================ CREATE ALL SUBSCRIPTION PLANS ===================== //
    @PostMapping("/bulk")
    public ResponseEntity<?> saveAllSubscriptionPlans(@RequestBody List<SubscriptionPlan> subscriptionPlans) {
        return new ResponseEntity<>(this.SUBSCRIPTION_PLAN_SERVICE_IMPL.saveAll(subscriptionPlans), HttpStatus.CREATED);
    }

    // ================ GET SUBSCRIPTION PLAN BY ID ===================== //
    @GetMapping("/{identifier}")
    public ResponseEntity<?> getSubscriptionPlanById(@PathVariable String identifier) {
        return new ResponseEntity<>(this.SUBSCRIPTION_PLAN_SERVICE_IMPL.findById(identifier), HttpStatus.OK);
    }

    // ================ GET ALL SUBSCRIPTION PLANS ===================== //
    @GetMapping
    public ResponseEntity<?> getAllSubscriptionPlans() {
        List<SubscriptionPlan> allPlans = this.SUBSCRIPTION_PLAN_SERVICE_IMPL.findAll();
        return new ResponseEntity<>(allPlans, HttpStatus.OK);
    }

    // ================ DELETE SUBSCRIPTION PLAN BY ID ===================== //
    @DeleteMapping("/{identifier}")
    public ResponseEntity<?> deleteSubscriptionPlanById(@PathVariable String identifier) {
        this.SUBSCRIPTION_PLAN_SERVICE_IMPL.deleteById(identifier);
        return new ResponseEntity<>("SubscriptionPlan deleted successfully", HttpStatus.OK);
    }

    // ================ UPDATE SUBSCRIPTION PLAN BY ID ===================== //
    @PutMapping("/{identifier}")
    public ResponseEntity<?> updateSubscriptionPlanById(@PathVariable String identifier, @RequestBody SubscriptionPlan subscriptionPlan) {
        if (!this.SUBSCRIPTION_PLAN_SERVICE_IMPL.existsById(identifier)) {
            return new ResponseEntity<>("SubscriptionPlan not found", HttpStatus.NOT_FOUND);
        }
        subscriptionPlan.setIdentifier(identifier);
        return new ResponseEntity<>(this.SUBSCRIPTION_PLAN_SERVICE_IMPL.save(subscriptionPlan), HttpStatus.OK);
    }

    // ================ FIND BY NAME (TIER) ===================== //
    @GetMapping("/tier/{name}")
    public ResponseEntity<?> findByName(@PathVariable SubscriptionTier name) {
        return new ResponseEntity<>(this.SUBSCRIPTION_PLAN_SERVICE_IMPL.findByName(name), HttpStatus.OK);
    }
}
