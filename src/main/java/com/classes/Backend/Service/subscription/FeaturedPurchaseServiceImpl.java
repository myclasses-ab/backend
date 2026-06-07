package com.classes.Backend.Service.subscription;

import com.classes.Backend.Domain.enums.CreditTransactionType;
import com.classes.Backend.Domain.enums.FeaturedPurchaseStatus;
import com.classes.Backend.Domain.institute.Institute;
import com.classes.Backend.Domain.subscription.FeaturedPurchase;
import com.classes.Backend.Repository.institute.InstituteRepository;
import com.classes.Backend.Repository.subscription.FeaturedPurchaseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

@RequiredArgsConstructor
@Service
public class FeaturedPurchaseServiceImpl implements FeaturedPurchaseService {

    private final FeaturedPurchaseRepository FEATURED_PURCHASE_REPOSITORY;
    private final InstituteRepository INSTITUTE_REPOSITORY;
    private final CreditServiceImpl CREDIT_SERVICE_IMPL;

    @Value("${credits.featured-cost-per-month:500}")
    private Integer featuredCostPerMonth;

    @Value("${credits.featured-duration-days:30}")
    private Integer featuredDurationDays;

    @Override
    @Transactional
    public FeaturedPurchase purchase(String instituteIdentifier) {
        if (!CREDIT_SERVICE_IMPL.hasSufficientBalance(instituteIdentifier, featuredCostPerMonth)) {
            throw new IllegalStateException("Insufficient credit balance. Required: " + featuredCostPerMonth);
        }

        List<FeaturedPurchase> activePurchases = FEATURED_PURCHASE_REPOSITORY
                .findByInstituteIdentifierAndStatus(instituteIdentifier, FeaturedPurchaseStatus.ACTIVE);

        LocalDateTime now = LocalDateTime.now();
        FeaturedPurchase purchase;

        if (!activePurchases.isEmpty()) {
            // Extend the latest expiry by durationDays
            FeaturedPurchase latest = activePurchases.stream()
                    .max(Comparator.comparing(FeaturedPurchase::getExpiresAt))
                    .orElseThrow();
            latest.setExpiresAt(latest.getExpiresAt().plusDays(featuredDurationDays));
            purchase = FEATURED_PURCHASE_REPOSITORY.save(latest);
        } else {
            purchase = new FeaturedPurchase();
            purchase.setInstituteIdentifier(instituteIdentifier);
            purchase.setCost(featuredCostPerMonth);
            purchase.setDurationDays(featuredDurationDays);
            purchase.setStatus(FeaturedPurchaseStatus.ACTIVE);
            purchase.setExpiresAt(now.plusDays(featuredDurationDays));
            purchase = FEATURED_PURCHASE_REPOSITORY.save(purchase);

            // Set institute as featured
            Institute institute = INSTITUTE_REPOSITORY.findById(instituteIdentifier)
                    .orElseThrow(() -> new RuntimeException("Institute not found with identifier: " + instituteIdentifier));
            institute.setIsFeatured(true);
            INSTITUTE_REPOSITORY.save(institute);
        }

        CREDIT_SERVICE_IMPL.deductCredits(
                instituteIdentifier,
                featuredCostPerMonth,
                CreditTransactionType.DEDUCTED_FOR_FEATURED,
                "Deducted for featured badge purchase: " + featuredDurationDays + " days",
                purchase.getIdentifier()
        );

        return purchase;
    }

    @Override
    public List<FeaturedPurchase> findByInstitute(String instituteIdentifier) {
        return FEATURED_PURCHASE_REPOSITORY.findByInstituteIdentifier(instituteIdentifier);
    }

    @Override
    public List<FeaturedPurchase> findActive() {
        return FEATURED_PURCHASE_REPOSITORY.findByStatus(FeaturedPurchaseStatus.ACTIVE);
    }

    @Override
    public List<FeaturedPurchase> findAll() {
        return FEATURED_PURCHASE_REPOSITORY.findAll();
    }

    @Override
    @Transactional
    public void expirePurchases() {
        LocalDateTime now = LocalDateTime.now();
        List<FeaturedPurchase> expired = FEATURED_PURCHASE_REPOSITORY
                .findByStatusAndExpiresAtBefore(FeaturedPurchaseStatus.ACTIVE, now);

        for (FeaturedPurchase purchase : expired) {
            purchase.setStatus(FeaturedPurchaseStatus.EXPIRED);
            FEATURED_PURCHASE_REPOSITORY.save(purchase);

            // Check if institute has any other active purchases
            List<FeaturedPurchase> remainingActive = FEATURED_PURCHASE_REPOSITORY
                    .findByInstituteIdentifierAndStatus(purchase.getInstituteIdentifier(), FeaturedPurchaseStatus.ACTIVE);

            if (remainingActive.isEmpty()) {
                Institute institute = INSTITUTE_REPOSITORY.findById(purchase.getInstituteIdentifier()).orElse(null);
                if (institute != null) {
                    institute.setIsFeatured(false);
                    INSTITUTE_REPOSITORY.save(institute);
                }
            }
        }
    }
}
