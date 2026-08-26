package com.classes.Backend.Controller.institute;

import com.classes.Backend.Domain.activity.ActivityActionType;
import com.classes.Backend.Domain.activity.ActivityEntityType;
import com.classes.Backend.Domain.enums.InstituteType;
import com.classes.Backend.Domain.enums.OwnershipType;
import com.classes.Backend.Domain.enums.SubscriptionTier;
import com.classes.Backend.Domain.institute.Institute;
import com.classes.Backend.Service.activity.ActivityLogActorResolver;
import com.classes.Backend.Service.activity.ActivityLogChangeExtractor;
import com.classes.Backend.Service.activity.ActivityLogService;
import com.classes.Backend.Service.activity.ResolvedActor;
import com.classes.Backend.Service.institute.InstituteServiceImpl;
import com.classes.Backend.dto.activity.ActivityLogRequest;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/institutes")
public class InstituteController {

    private final InstituteServiceImpl INSTITUTE_SERVICE_IMPL;
    private final ActivityLogService ACTIVITY_LOG_SERVICE;
    private final ActivityLogActorResolver ACTOR_RESOLVER;

    // ================ CREATE INSTITUTE ===================== //
    @PostMapping
    public ResponseEntity<?> saveInstitute(@RequestBody Institute institute, HttpServletRequest request) {
        // Always generate UUID for identifier - ignore any client-provided value
        institute.setIdentifier(UUID.randomUUID().toString());

        // Generate slug from name if not provided
        if (institute.getSlug() == null || institute.getSlug().isBlank()) {
            institute.setSlug(generateSlug(institute.getName()));
        }

        Institute saved = this.INSTITUTE_SERVICE_IMPL.save(institute);

        ResolvedActor actor = ACTOR_RESOLVER.resolve(request);
        if (actor.isAuthenticated()) {
            ACTIVITY_LOG_SERVICE.log(ActivityLogRequest.builder()
                    .actorType(actor.getType())
                    .actorIdentifier(actor.getIdentifier())
                    .actorName(actor.getName())
                    .actionType(ActivityActionType.INSTITUTE_CREATED)
                    .entityType(ActivityEntityType.INSTITUTE)
                    .entityIdentifier(saved.getIdentifier())
                    .entityName(saved.getName())
                    .instituteIdentifier(saved.getIdentifier())
                    .description("Created institute " + saved.getName())
                    .source("CONSOLE")
                    .build());
        }

        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }

    // ================ CREATE ALL INSTITUTES ===================== //
    @PostMapping("/bulk")
    public ResponseEntity<?> saveAllInstitutes(@RequestBody List<Institute> institutes) {
        for (Institute institute : institutes) {
            // Always generate UUID for identifier - ignore any client-provided value
            institute.setIdentifier(UUID.randomUUID().toString());
            
            // Generate slug from name if not provided
            if (institute.getSlug() == null || institute.getSlug().isBlank()) {
                institute.setSlug(generateSlug(institute.getName()));
            }
        }
        return new ResponseEntity<>(this.INSTITUTE_SERVICE_IMPL.saveAll(institutes), HttpStatus.CREATED);
    }

    // ================ GET INSTITUTE BY ID ===================== //
    @GetMapping("/{identifier}")
    public ResponseEntity<?> getInstituteById(@PathVariable String identifier) {
        return new ResponseEntity<>(this.INSTITUTE_SERVICE_IMPL.findById(identifier), HttpStatus.OK);
    }

    // ================ GET ALL INSTITUTES ===================== //
    @GetMapping
    public ResponseEntity<?> getAllInstitutes() {
        List<Institute> allInstitutes = this.INSTITUTE_SERVICE_IMPL.findAll();
        return new ResponseEntity<>(allInstitutes, HttpStatus.OK);
    }

    // ================ DELETE INSTITUTE BY ID ===================== //
    @DeleteMapping("/{identifier}")
    public ResponseEntity<?> deleteInstituteById(@PathVariable String identifier, HttpServletRequest request) {
        String nameBeforeDelete = this.INSTITUTE_SERVICE_IMPL.findById(identifier)
                .map(Institute::getName)
                .orElse(null);

        this.INSTITUTE_SERVICE_IMPL.deleteById(identifier);

        ResolvedActor actor = ACTOR_RESOLVER.resolve(request);
        if (actor.isAuthenticated()) {
            ACTIVITY_LOG_SERVICE.log(ActivityLogRequest.builder()
                    .actorType(actor.getType())
                    .actorIdentifier(actor.getIdentifier())
                    .actorName(actor.getName())
                    .actionType(ActivityActionType.INSTITUTE_DELETED)
                    .entityType(ActivityEntityType.INSTITUTE)
                    .entityIdentifier(identifier)
                    .entityName(nameBeforeDelete)
                    .instituteIdentifier(identifier)
                    .description("Deleted institute" + (nameBeforeDelete != null ? " " + nameBeforeDelete : ""))
                    .source("SUPER_ADMIN")
                    .build());
        }

        return new ResponseEntity<>("Institute deleted successfully", HttpStatus.OK);
    }

    // ================ UPDATE INSTITUTETYPE BY ID ===================== //
    @PutMapping("/{identifier}")
    public ResponseEntity<?> updateInstituteById(@PathVariable String identifier, @RequestBody Institute institute, HttpServletRequest request) {
        Institute existing = this.INSTITUTE_SERVICE_IMPL.findById(identifier).orElse(null);
        if (existing == null) {
            return new ResponseEntity<>("InstituteType not found", HttpStatus.NOT_FOUND);
        }
        institute.setIdentifier(identifier);
        Institute updated = this.INSTITUTE_SERVICE_IMPL.save(institute);

        ResolvedActor actor = ACTOR_RESOLVER.resolve(request);
        if (actor.isAuthenticated()) {
            ACTIVITY_LOG_SERVICE.log(ActivityLogRequest.builder()
                    .actorType(actor.getType())
                    .actorIdentifier(actor.getIdentifier())
                    .actorName(actor.getName())
                    .actionType(ActivityActionType.INSTITUTE_UPDATED)
                    .entityType(ActivityEntityType.INSTITUTE)
                    .entityIdentifier(updated.getIdentifier())
                    .entityName(updated.getName())
                    .instituteIdentifier(updated.getIdentifier())
                    .description("Updated institute profile")
                    .metadata(Map.of("changedFields", ActivityLogChangeExtractor.extractChangedFields(existing, updated)))
                    .source("CONSOLE")
                    .build());
        }

        return new ResponseEntity<>(updated, HttpStatus.OK);
    }

    // ================ FIND BY SLUG ===================== //
    @GetMapping("/slug/{slug}")
    public ResponseEntity<?> findBySlug(@PathVariable String slug) {
        return new ResponseEntity<>(this.INSTITUTE_SERVICE_IMPL.findBySlug(slug), HttpStatus.OK);
    }

    // ================ FIND BY TYPE ===================== //
    @GetMapping("/type/{type}")
    public ResponseEntity<?> findByType(@PathVariable InstituteType type) {
        return new ResponseEntity<>(this.INSTITUTE_SERVICE_IMPL.findByType(type), HttpStatus.OK);
    }

    // ================ FIND BY OWNERSHIP TYPE ===================== //
    @GetMapping("/ownership/{ownershipType}")
    public ResponseEntity<?> findByOwnershipType(@PathVariable OwnershipType ownershipType) {
        return new ResponseEntity<>(this.INSTITUTE_SERVICE_IMPL.findByOwnershipType(ownershipType), HttpStatus.OK);
    }

    // ================ FIND BY SUBSCRIPTION TIER ===================== //
    @GetMapping("/subscription-tier/{subscriptionTier}")
    public ResponseEntity<?> findBySubscriptionTier(@PathVariable SubscriptionTier subscriptionTier) {
        return new ResponseEntity<>(this.INSTITUTE_SERVICE_IMPL.findBySubscriptionTier(subscriptionTier), HttpStatus.OK);
    }

    // ================ FIND VERIFIED INSTITUTES ===================== //
    @GetMapping("/verified")
    public ResponseEntity<?> findByIsVerifiedTrue() {
        return new ResponseEntity<>(this.INSTITUTE_SERVICE_IMPL.findByIsVerifiedTrue(), HttpStatus.OK);
    }

    // ================ FIND FEATURED INSTITUTES ===================== //
    @GetMapping("/featured")
    public ResponseEntity<?> findByIsFeaturedTrue() {
        return new ResponseEntity<>(this.INSTITUTE_SERVICE_IMPL.findByIsFeaturedTrue(), HttpStatus.OK);
    }

    // ================ FIND ACTIVE INSTITUTES ===================== //
    @GetMapping("/active")
    public ResponseEntity<?> findByIsActiveTrue() {
        return new ResponseEntity<>(this.INSTITUTE_SERVICE_IMPL.findByIsActiveTrue(), HttpStatus.OK);
    }

    // ================ FIND BY PARENT INSTITUTE IDENTIFIER ===================== //
    @GetMapping("/parent/{parentInstituteIdentifier}")
    public ResponseEntity<?> findByParentInstituteIdentifier(@PathVariable String parentInstituteIdentifier) {
        return new ResponseEntity<>(this.INSTITUTE_SERVICE_IMPL.findByParentInstituteIdentifier(parentInstituteIdentifier), HttpStatus.OK);
    }

    // ================ SEARCH INSTITUTES ===================== //
    @GetMapping("/search")
    public ResponseEntity<?> searchInstitutes(
            @RequestParam(required = false) String query,
            @RequestParam(required = false) String cityIdentifier,
            @RequestParam(required = false) String cityName,
            @RequestParam(required = false) BigDecimal minFee,
            @RequestParam(required = false) BigDecimal maxFee,
            @RequestParam(required = false) BigDecimal minRating,
            @RequestParam(required = false) InstituteType type,
            @RequestParam(required = false) SubscriptionTier subscriptionTier,
            @RequestParam(required = false) Boolean isVerified,
            @RequestParam(required = false) Boolean isFeatured,
            @RequestParam(required = false) Boolean hasHostel,
            @RequestParam(required = false, defaultValue = "relevance") String sortBy,
            @RequestParam(required = false, defaultValue = "desc") String sortOrder,
            @RequestParam(required = false) BigDecimal userLat,
            @RequestParam(required = false) BigDecimal userLng,
            @RequestParam(required = false) BigDecimal radiusKm
    ) {
        if (query != null && query.trim().isEmpty()) {
            query = null;
        }
        if (cityName != null && cityName.trim().isEmpty()) {
            cityName = null;
        }
        List<Institute> results = this.INSTITUTE_SERVICE_IMPL.searchInstitutes(
                query, cityIdentifier, cityName, minFee, maxFee, minRating, type, subscriptionTier,
                isVerified, isFeatured, hasHostel, sortBy, sortOrder,
                userLat, userLng, radiusKm
        );
        return new ResponseEntity<>(results, HttpStatus.OK);
    }

    private String generateSlug(String name) {
        if (name == null || name.trim().isEmpty()) {
            return "institute";
        }
        String slug = name.trim()
                .toLowerCase()
                .replaceAll("[^a-z0-9\\s-]", "")
                .replaceAll("\\s+", "-")
                .replaceAll("-+", "-")
                .replaceAll("^-*|-*$", "");
        return slug.isEmpty() ? "institute" : slug;
    }
}
