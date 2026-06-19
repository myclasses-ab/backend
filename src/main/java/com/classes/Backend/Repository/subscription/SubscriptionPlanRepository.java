package com.classes.Backend.Repository.subscription;

import com.classes.Backend.Domain.subscription.SubscriptionPlan;
import com.classes.Backend.Domain.enums.SubscriptionTier;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SubscriptionPlanRepository extends JpaRepository<SubscriptionPlan, String> {
    Optional<SubscriptionPlan> findByName(SubscriptionTier name);
}
