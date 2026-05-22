package com.classes.Backend.Repository.institute;

import com.classes.Backend.Domain.institute.Institute;
import com.classes.Backend.Domain.enums.InstituteType;
import com.classes.Backend.Domain.enums.OwnershipType;
import com.classes.Backend.Domain.enums.SubscriptionTier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface InstituteRepository extends JpaRepository<Institute, String> {
    Optional<Institute> findBySlug(String slug);
    List<Institute> findByType(InstituteType type);
    List<Institute> findByOwnershipType(OwnershipType ownershipType);
    List<Institute> findBySubscriptionTier(SubscriptionTier subscriptionTier);
    List<Institute> findByIsVerifiedTrue();
    List<Institute> findByIsFeaturedTrue();
    List<Institute> findByIsActiveTrue();
    List<Institute> findByParentInstituteIdentifier(String parentInstituteIdentifier);

    @Query(value = """
        SELECT DISTINCT i.* FROM institutes i
        LEFT JOIN branches b ON b.institute_identifier = i.identifier
        LEFT JOIN institute_courses ic ON ic.institute_identifier = i.identifier
        LEFT JOIN institute_facilities f ON f.institute_identifier = i.identifier
        WHERE i.is_active = true
          AND (:query IS NULL OR i.name ILIKE CONCAT('%', :query, '%') OR ic.custom_name ILIKE CONCAT('%', :query, '%') OR i.tagline ILIKE CONCAT('%', :query, '%') OR i.description ILIKE CONCAT('%', :query, '%'))
          AND (:cityIdentifier IS NULL OR b.city_identifier = :cityIdentifier)
          AND (:cityName IS NULL OR b.city_name ILIKE CONCAT('%', :cityName, '%'))
          AND (:minFee IS NULL OR ic.fee_min >= :minFee)
          AND (:maxFee IS NULL OR ic.fee_max <= :maxFee)
          AND (:minRating IS NULL OR i.average_rating >= :minRating)
          AND (:type IS NULL OR i.type = :type)
          AND (:subscriptionTier IS NULL OR i.subscription_tier = :subscriptionTier)
          AND (:isVerified IS NULL OR i.is_verified = :isVerified)
          AND (:isFeatured IS NULL OR i.is_featured = :isFeatured)
          AND (:hasHostel IS NULL OR f.has_hostel = :hasHostel)
        """, nativeQuery = true)
    List<Institute> searchInstitutes(
        @Param("query") String query,
        @Param("cityIdentifier") String cityIdentifier,
        @Param("cityName") String cityName,
        @Param("minFee") BigDecimal minFee,
        @Param("maxFee") BigDecimal maxFee,
        @Param("minRating") BigDecimal minRating,
        @Param("type") String type,
        @Param("subscriptionTier") String subscriptionTier,
        @Param("isVerified") Boolean isVerified,
        @Param("isFeatured") Boolean isFeatured,
        @Param("hasHostel") Boolean hasHostel
    );
}
