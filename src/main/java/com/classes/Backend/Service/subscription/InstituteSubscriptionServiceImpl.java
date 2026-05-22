package com.classes.Backend.Service.subscription;

import com.classes.Backend.Domain.subscription.InstituteSubscription;
import com.classes.Backend.Repository.subscription.InstituteSubscriptionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class InstituteSubscriptionServiceImpl implements InstituteSubscriptionService {
    private final InstituteSubscriptionRepository INSTITUTE_SUBSCRIPTION_REPOSITORY;

    // ================ SAVE INSTITUTE SUBSCRIPTION ===================== //
    @Override
    public InstituteSubscription save(InstituteSubscription instituteSubscription) {
        return this.INSTITUTE_SUBSCRIPTION_REPOSITORY.save(instituteSubscription);
    }

    // ================ SAVE ALL INSTITUTE SUBSCRIPTIONS ===================== //
    @Override
    public List<InstituteSubscription> saveAll(List<InstituteSubscription> instituteSubscriptions) {
        return this.INSTITUTE_SUBSCRIPTION_REPOSITORY.saveAll(instituteSubscriptions);
    }

    // ================ FIND BY ID ===================== //
    @Override
    public Optional<InstituteSubscription> findById(String identifier) {
        return this.INSTITUTE_SUBSCRIPTION_REPOSITORY.findById(identifier);
    }

    // ================ FIND ALL ===================== //
    @Override
    public List<InstituteSubscription> findAll() {
        return this.INSTITUTE_SUBSCRIPTION_REPOSITORY.findAll();
    }

    // ================ DELETE BY ID ===================== //
    @Override
    public void deleteById(String identifier) {
        if (!this.INSTITUTE_SUBSCRIPTION_REPOSITORY.existsById(identifier)) {
            throw new RuntimeException("InstituteSubscription with identifier '" + identifier + "' not found");
        }
        this.INSTITUTE_SUBSCRIPTION_REPOSITORY.deleteById(identifier);
    }

    // ================ EXISTS BY ID ===================== //
    @Override
    public boolean existsById(String identifier) {
        return this.INSTITUTE_SUBSCRIPTION_REPOSITORY.existsById(identifier);
    }

    // ================ FIND BY INSTITUTE IDENTIFIER ===================== //
    @Override
    public Optional<InstituteSubscription> findByInstituteIdentifier(String instituteIdentifier) {
        return this.INSTITUTE_SUBSCRIPTION_REPOSITORY.findByInstituteIdentifier(instituteIdentifier);
    }

    // ================ FIND BY PLAN IDENTIFIER ===================== //
    @Override
    public List<InstituteSubscription> findByPlanIdentifier(String planIdentifier) {
        return this.INSTITUTE_SUBSCRIPTION_REPOSITORY.findByPlanIdentifier(planIdentifier);
    }

    // ================ FIND BY IS ACTIVE TRUE ===================== //
    @Override
    public List<InstituteSubscription> findByIsActiveTrue() {
        return this.INSTITUTE_SUBSCRIPTION_REPOSITORY.findByIsActiveTrue();
    }
}
