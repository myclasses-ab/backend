package com.classes.Backend.Service.subscription;

import com.classes.Backend.Domain.subscription.SubscriptionPlan;
import com.classes.Backend.Domain.enums.SubscriptionTier;
import com.classes.Backend.Repository.subscription.SubscriptionPlanRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class SubscriptionPlanServiceImpl implements SubscriptionPlanService {
    private final SubscriptionPlanRepository SUBSCRIPTION_PLAN_REPOSITORY;

    // ================ SAVE SUBSCRIPTION PLAN ===================== //
    @Override
    public SubscriptionPlan save(SubscriptionPlan subscriptionPlan) {
        return this.SUBSCRIPTION_PLAN_REPOSITORY.save(subscriptionPlan);
    }

    // ================ SAVE ALL SUBSCRIPTION PLANS ===================== //
    @Override
    public List<SubscriptionPlan> saveAll(List<SubscriptionPlan> subscriptionPlans) {
        return this.SUBSCRIPTION_PLAN_REPOSITORY.saveAll(subscriptionPlans);
    }

    // ================ FIND BY ID ===================== //
    @Override
    public Optional<SubscriptionPlan> findById(String identifier) {
        return this.SUBSCRIPTION_PLAN_REPOSITORY.findById(identifier);
    }

    // ================ FIND ALL ===================== //
    @Override
    public List<SubscriptionPlan> findAll() {
        return this.SUBSCRIPTION_PLAN_REPOSITORY.findAll();
    }

    // ================ DELETE BY ID ===================== //
    @Override
    public void deleteById(String identifier) {
        if (!this.SUBSCRIPTION_PLAN_REPOSITORY.existsById(identifier)) {
            throw new RuntimeException("SubscriptionPlan with identifier '" + identifier + "' not found");
        }
        this.SUBSCRIPTION_PLAN_REPOSITORY.deleteById(identifier);
    }

    // ================ EXISTS BY ID ===================== //
    @Override
    public boolean existsById(String identifier) {
        return this.SUBSCRIPTION_PLAN_REPOSITORY.existsById(identifier);
    }

    // ================ FIND BY NAME ===================== //
    @Override
    public Optional<SubscriptionPlan> findByName(SubscriptionTier name) {
        return this.SUBSCRIPTION_PLAN_REPOSITORY.findByName(name);
    }
}
