package com.classes.Backend.Service.institute;

import com.classes.Backend.Domain.institute.Institute;
import com.classes.Backend.Domain.enums.InstituteType;
import com.classes.Backend.Domain.enums.OwnershipType;
import com.classes.Backend.Domain.enums.SubscriptionTier;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface InstituteService {
    // ================ CRUD OPERATIONS ===================== //
    Institute save(Institute institute);
    List<Institute> saveAll(List<Institute> institutes);
    Optional<Institute> findById(String identifier);
    List<Institute> findAll();
    void deleteById(String identifier);
    boolean existsById(String identifier);

    // ================ CUSTOM FINDER METHODS ===================== //
    Optional<Institute> findBySlug(String slug);
    List<Institute> findByType(InstituteType type);
    List<Institute> findByOwnershipType(OwnershipType ownershipType);
    List<Institute> findBySubscriptionTier(SubscriptionTier subscriptionTier);
    List<Institute> findByIsVerifiedTrue();
    List<Institute> findByIsFeaturedTrue();
    List<Institute> findByIsActiveTrue();
    List<Institute> findByParentInstituteIdentifier(String parentInstituteIdentifier);

    // ================ SEARCH ===================== //
    List<Institute> searchInstitutes(String query, String cityIdentifier, String cityName, BigDecimal minFee, BigDecimal maxFee, BigDecimal minRating, InstituteType type, SubscriptionTier subscriptionTier, Boolean isVerified, Boolean isFeatured, Boolean hasHostel, String sortBy, String sortOrder, BigDecimal userLat, BigDecimal userLng, BigDecimal radiusKm);
}
