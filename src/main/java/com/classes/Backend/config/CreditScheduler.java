package com.classes.Backend.config;

import com.classes.Backend.Service.subscription.FeaturedPurchaseServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class CreditScheduler {

    private final FeaturedPurchaseServiceImpl FEATURED_PURCHASE_SERVICE_IMPL;

    @Scheduled(cron = "0 0 2 * * ?")
    public void expireFeaturedPurchases() {
        log.info("Running scheduled featured purchase expiry check...");
        FEATURED_PURCHASE_SERVICE_IMPL.expirePurchases();
        log.info("Featured purchase expiry check completed.");
    }
}
