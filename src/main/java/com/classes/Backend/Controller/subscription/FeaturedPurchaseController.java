package com.classes.Backend.Controller.subscription;

import com.classes.Backend.Domain.activity.ActivityActionType;
import com.classes.Backend.Domain.activity.ActivityEntityType;
import com.classes.Backend.Domain.subscription.FeaturedPurchase;
import com.classes.Backend.Service.activity.ActivityLogActorResolver;
import com.classes.Backend.Service.activity.ActivityLogService;
import com.classes.Backend.Service.activity.ResolvedActor;
import com.classes.Backend.Service.subscription.FeaturedPurchaseServiceImpl;
import com.classes.Backend.dto.activity.ActivityLogRequest;
import com.classes.Backend.dto.credits.BuyFeaturedRequest;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/featured-purchases")
public class FeaturedPurchaseController {

    private final FeaturedPurchaseServiceImpl FEATURED_PURCHASE_SERVICE_IMPL;
    private final ActivityLogService ACTIVITY_LOG_SERVICE;
    private final ActivityLogActorResolver ACTOR_RESOLVER;

    @PostMapping
    public ResponseEntity<?> buyFeatured(@RequestBody BuyFeaturedRequest request, HttpServletRequest httpRequest) {
        try {
            FeaturedPurchase purchase = FEATURED_PURCHASE_SERVICE_IMPL.purchase(request.getInstituteIdentifier());

            ResolvedActor actor = ACTOR_RESOLVER.resolve(httpRequest);
            if (actor.isAuthenticated()) {
                ACTIVITY_LOG_SERVICE.log(ActivityLogRequest.builder()
                        .actorType(actor.getType())
                        .actorIdentifier(actor.getIdentifier())
                        .actorName(actor.getName())
                        .actionType(ActivityActionType.FEATURED_PURCHASED)
                        .entityType(ActivityEntityType.FEATURED_PURCHASE)
                        .entityIdentifier(purchase.getIdentifier())
                        .entityName("Featured Purchase")
                        .instituteIdentifier(purchase.getInstituteIdentifier())
                        .description("Purchased featured listing for " + purchase.getDurationDays() + " days")
                        .metadata(Map.of(
                                "cost", purchase.getCost(),
                                "durationDays", purchase.getDurationDays()
                        ))
                        .source("CONSOLE")
                        .build());
            }

            return new ResponseEntity<>(purchase, HttpStatus.CREATED);
        } catch (IllegalArgumentException | IllegalStateException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping
    public ResponseEntity<?> getAllFeaturedPurchases() {
        return ResponseEntity.ok(FEATURED_PURCHASE_SERVICE_IMPL.findAll());
    }

    @GetMapping("/institute/{instituteIdentifier}")
    public ResponseEntity<?> getFeaturedPurchasesByInstitute(@PathVariable String instituteIdentifier) {
        return ResponseEntity.ok(FEATURED_PURCHASE_SERVICE_IMPL.findByInstitute(instituteIdentifier));
    }

    @GetMapping("/active")
    public ResponseEntity<?> getActiveFeaturedPurchases() {
        return ResponseEntity.ok(FEATURED_PURCHASE_SERVICE_IMPL.findActive());
    }
}
