package com.classes.Backend.Controller.institute;

import com.classes.Backend.Domain.enums.InstituteType;
import com.classes.Backend.Domain.enums.OwnershipType;
import com.classes.Backend.Domain.enums.SubscriptionTier;
import com.classes.Backend.Domain.institute.Institute;
import com.classes.Backend.Service.institute.InstituteServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/institutes")
public class InstituteController {

    private final InstituteServiceImpl INSTITUTE_SERVICE_IMPL;

    // ================ CREATE INSTITUTE ===================== //
    @PostMapping
    public ResponseEntity<?> saveInstitute(@RequestBody Institute institute) {
        // Always generate UUID for identifier - ignore any client-provided value
        institute.setIdentifier(UUID.randomUUID().toString());
        
        // Generate slug from name if not provided
        if (institute.getSlug() == null || institute.getSlug().isBlank()) {
            institute.setSlug(generateSlug(institute.getName()));
        }
        
        return new ResponseEntity<>(this.INSTITUTE_SERVICE_IMPL.save(institute), HttpStatus.CREATED);
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
    public ResponseEntity<?> deleteInstituteById(@PathVariable String identifier) {
        this.INSTITUTE_SERVICE_IMPL.deleteById(identifier);
        return new ResponseEntity<>("Institute deleted successfully", HttpStatus.OK);
    }

    // ================ UPDATE INSTITUTETYPE BY ID ===================== //
    @PutMapping("/{identifier}")
    public ResponseEntity<?> updateInstituteById(@PathVariable String identifier, @RequestBody Institute institute) {
        if (!this.INSTITUTE_SERVICE_IMPL.existsById(identifier)) {
            return new ResponseEntity<>("InstituteType not found", HttpStatus.NOT_FOUND);
        }
        institute.setIdentifier(identifier);
        return new ResponseEntity<>(this.INSTITUTE_SERVICE_IMPL.save(institute), HttpStatus.OK);
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
            @RequestParam(required = false, defaultValue = "desc") String sortOrder
    ) {
        if (query != null && query.trim().isEmpty()) {
            query = null;
        }
        if (cityName != null && cityName.trim().isEmpty()) {
            cityName = null;
        }
        List<Institute> results = this.INSTITUTE_SERVICE_IMPL.searchInstitutes(
                query, cityIdentifier, cityName, minFee, maxFee, minRating, type, subscriptionTier,
                isVerified, isFeatured, hasHostel, sortBy, sortOrder
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
