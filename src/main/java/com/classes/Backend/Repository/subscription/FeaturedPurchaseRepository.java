package com.classes.Backend.Repository.subscription;

import com.classes.Backend.Domain.enums.FeaturedPurchaseStatus;
import com.classes.Backend.Domain.subscription.FeaturedPurchase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface FeaturedPurchaseRepository extends JpaRepository<FeaturedPurchase, String> {
    List<FeaturedPurchase> findByInstituteIdentifier(String instituteIdentifier);
    List<FeaturedPurchase> findByStatus(FeaturedPurchaseStatus status);
    List<FeaturedPurchase> findByStatusAndExpiresAtBefore(FeaturedPurchaseStatus status, LocalDateTime expiresAt);
    List<FeaturedPurchase> findByInstituteIdentifierAndStatus(String instituteIdentifier, FeaturedPurchaseStatus status);
}
