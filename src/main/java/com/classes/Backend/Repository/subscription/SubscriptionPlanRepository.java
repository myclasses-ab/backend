package com.classes.Backend.Repository.subscription;

import com.classes.Backend.Domain.subscription.SubscriptionPlan;
import com.classes.Backend.Domain.enums.SubscriptionTier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SubscriptionPlanRepository extends JpaRepository<SubscriptionPlan, String> {
    Optional<SubscriptionPlan> findByName(SubscriptionTier name);
}
