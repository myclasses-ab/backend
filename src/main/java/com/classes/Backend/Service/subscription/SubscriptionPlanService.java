package com.classes.Backend.Service.subscription;

import com.classes.Backend.Domain.subscription.SubscriptionPlan;
import com.classes.Backend.Domain.enums.SubscriptionTier;

import java.util.List;
import java.util.Optional;

public interface SubscriptionPlanService {
    // ================ CRUD OPERATIONS ===================== //
    SubscriptionPlan save(SubscriptionPlan subscriptionPlan);
    List<SubscriptionPlan> saveAll(List<SubscriptionPlan> subscriptionPlans);
    Optional<SubscriptionPlan> findById(String identifier);
    List<SubscriptionPlan> findAll();
    void deleteById(String identifier);
    boolean existsById(String identifier);

    // ================ CUSTOM FINDER METHODS ===================== //
    Optional<SubscriptionPlan> findByName(SubscriptionTier name);
}
