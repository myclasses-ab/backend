package com.classes.Backend.Repository.subscription;

import com.classes.Backend.Domain.subscription.InstituteSubscription;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface InstituteSubscriptionRepository extends JpaRepository<InstituteSubscription, String> {
    Optional<InstituteSubscription> findByInstituteIdentifier(String instituteIdentifier);
    List<InstituteSubscription> findByPlanIdentifier(String planIdentifier);
    List<InstituteSubscription> findByIsActiveTrue();
}
