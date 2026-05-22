package com.classes.Backend.Service.subscription;

import com.classes.Backend.Domain.subscription.InstituteSubscription;

import java.util.List;
import java.util.Optional;

public interface InstituteSubscriptionService {
    // ================ CRUD OPERATIONS ===================== //
    InstituteSubscription save(InstituteSubscription instituteSubscription);
    List<InstituteSubscription> saveAll(List<InstituteSubscription> instituteSubscriptions);
    Optional<InstituteSubscription> findById(String identifier);
    List<InstituteSubscription> findAll();
    void deleteById(String identifier);
    boolean existsById(String identifier);

    // ================ CUSTOM FINDER METHODS ===================== //
    Optional<InstituteSubscription> findByInstituteIdentifier(String instituteIdentifier);
    List<InstituteSubscription> findByPlanIdentifier(String planIdentifier);
    List<InstituteSubscription> findByIsActiveTrue();
}
