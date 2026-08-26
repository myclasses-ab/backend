package com.classes.Backend.Repository.institute;

import com.classes.Backend.Domain.institute.Branch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface BranchRepository extends JpaRepository<Branch, String> {
    List<Branch> findByInstituteIdentifier(String instituteIdentifier);
    List<Branch> findByCityIdentifier(String cityIdentifier);
    Optional<Branch> findByInstituteIdentifierAndIsMainBranchTrue(String instituteIdentifier);
    List<Branch> findByIsOnlineOnlyTrue();

    @Query(value = """
            SELECT b.* FROM branches b
            WHERE b.latitude IS NULL OR b.longitude IS NULL
            ORDER BY b.created_at DESC
            """, nativeQuery = true)
    List<Branch> findBranchesWithUnresolvedCoordinates();

    @Query(value = """
            SELECT b.*,
                (6371 * acos(
                    cos(radians(:userLat)) * cos(radians(b.latitude)) *
                    cos(radians(b.longitude) - radians(:userLng)) +
                    sin(radians(:userLat)) * sin(radians(b.latitude))
                )) AS distance_km
            FROM branches b
            WHERE b.latitude IS NOT NULL AND b.longitude IS NOT NULL
              AND (6371 * acos(
                    cos(radians(:userLat)) * cos(radians(b.latitude)) *
                    cos(radians(b.longitude) - radians(:userLng)) +
                    sin(radians(:userLat)) * sin(radians(b.latitude))
                )) <= :radiusKm
            ORDER BY distance_km ASC
            """, nativeQuery = true)
    List<Branch> findBranchesWithinRadius(
            @Param("userLat") BigDecimal userLat,
            @Param("userLng") BigDecimal userLng,
            @Param("radiusKm") BigDecimal radiusKm
    );
}
