package com.classes.Backend.Repository.institute;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.classes.Backend.Domain.enums.InstituteType;
import com.classes.Backend.Domain.enums.OwnershipType;
import com.classes.Backend.Domain.enums.SubscriptionTier;
import com.classes.Backend.Domain.institute.Institute;

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
        LEFT JOIN branch_service_cities bsc ON bsc.branch_identifier = b.identifier
        LEFT JOIN institute_courses ic ON ic.institute_identifier = i.identifier
        LEFT JOIN institute_facilities f ON f.institute_identifier = i.identifier
        WHERE i.is_active = true
          AND (:query IS NULL OR i.name ILIKE CONCAT('%', :query, '%') OR ic.custom_name ILIKE CONCAT('%', :query, '%') OR i.tagline ILIKE CONCAT('%', :query, '%') OR i.description ILIKE CONCAT('%', :query, '%'))
          AND (:cityIdentifier IS NULL OR b.city_identifier = :cityIdentifier)
          AND (:cityName IS NULL OR b.city_name ILIKE CONCAT('%', :cityName, '%') OR bsc.city_name ILIKE CONCAT('%', :cityName, '%'))
          AND (:minFee IS NULL OR ic.fee >= :minFee)
          AND (:maxFee IS NULL OR ic.fee <= :maxFee)
          AND (:minRating IS NULL OR i.average_rating >= :minRating)
          AND (:type IS NULL OR i.type = :type)
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
        @Param("isVerified") Boolean isVerified,
        @Param("isFeatured") Boolean isFeatured,
        @Param("hasHostel") Boolean hasHostel
    );
}
