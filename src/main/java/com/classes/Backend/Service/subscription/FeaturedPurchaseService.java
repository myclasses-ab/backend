package com.classes.Backend.Service.subscription;

import com.classes.Backend.Domain.subscription.FeaturedPurchase;

import java.util.List;

public interface FeaturedPurchaseService {
    FeaturedPurchase purchase(String instituteIdentifier);
    List<FeaturedPurchase> findByInstitute(String instituteIdentifier);
    List<FeaturedPurchase> findActive();
    List<FeaturedPurchase> findAll();
    void expirePurchases();
}
