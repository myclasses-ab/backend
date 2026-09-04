package com.classes.Backend.Service.institute;

import com.classes.Backend.Domain.institute.Branch;
import com.classes.Backend.Repository.institute.BranchRepository;
import com.classes.Backend.Service.location.GooglePlacesService;
import com.classes.Backend.Service.location.LocationResolverService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class BranchServiceImpl implements BranchService {
    private final BranchRepository BRANCH_REPOSITORY;
    private final LocationResolverService LOCATION_RESOLVER_SERVICE;
    private final GooglePlacesService GOOGLE_PLACES_SERVICE;

    // ================ SAVE BRANCH ===================== //
    @Override
    public Branch save(Branch branch) {
        validateGoogleMapsUrl(branch);
        resolveCoordinatesIfMissing(branch);
        resolveGoogleRating(branch);
        return this.BRANCH_REPOSITORY.save(branch);
    }

    private void validateGoogleMapsUrl(Branch branch) {
        String url = branch.getGoogleMapsUrl();
        if (!StringUtils.hasText(url)) {
            throw new IllegalArgumentException("Google Maps URL is required for every branch.");
        }
        try {
            new URL(url);
        } catch (Exception e) {
            throw new IllegalArgumentException("Google Maps URL must be a valid URL.");
        }
    }

    private void resolveCoordinatesIfMissing(Branch branch) {
        String mapsUrl = branch.getGoogleMapsUrl();
        if (!StringUtils.hasText(mapsUrl)) {
            return;
        }

        BigDecimal existingLatitude = branch.getLatitude();
        BigDecimal existingLongitude = branch.getLongitude();
        if (existingLatitude != null && existingLongitude != null) {
            return;
        }

        Optional<LocationResolverService.Coordinates> resolved = LOCATION_RESOLVER_SERVICE.resolve(mapsUrl);
        if (resolved.isPresent()) {
            LocationResolverService.Coordinates coordinates = resolved.get();
            branch.setLatitude(coordinates.latitude());
            branch.setLongitude(coordinates.longitude());
        } else {
            log.warn("Could not resolve coordinates for branch '{}' with maps URL: {}",
                    branch.getName(), mapsUrl);
        }
    }

    private void resolveGoogleRating(Branch branch) {
        String mapsUrl = branch.getGoogleMapsUrl();
        if (!StringUtils.hasText(mapsUrl)) {
            return;
        }

        Optional<GooglePlacesService.GooglePlaceInfo> placeInfo = GOOGLE_PLACES_SERVICE.fetchPlaceInfo(
                mapsUrl, branch.getLatitude(), branch.getLongitude(), branch.getName());

        if (placeInfo.isEmpty()) {
            log.warn("Could not fetch Google rating for branch '{}' with maps URL: {}",
                    branch.getName(), mapsUrl);
            return;
        }

        GooglePlacesService.GooglePlaceInfo info = placeInfo.get();
        branch.setGooglePlaceId(info.placeId());
        branch.setGoogleRating(info.rating());
        branch.setGoogleRatingCount(info.ratingCount());
        branch.setGoogleRatingUpdatedAt(LocalDateTime.now());

        // Prefer Google's own pin when URL-based coordinate resolution failed.
        if ((branch.getLatitude() == null || branch.getLongitude() == null)
                && info.latitude() != null && info.longitude() != null) {
            branch.setLatitude(info.latitude());
            branch.setLongitude(info.longitude());
        }
    }

    // ================ SAVE ALL BRANCHES ===================== //
    @Override
    public List<Branch> saveAll(List<Branch> branches) {
        if (branches != null) {
            branches.forEach(branch -> {
                validateGoogleMapsUrl(branch);
                resolveCoordinatesIfMissing(branch);
                resolveGoogleRating(branch);
            });
        }
        return this.BRANCH_REPOSITORY.saveAll(branches);
    }

    // ================ UPDATE BRANCH ===================== //
    @Override
    public Branch update(String identifier, Branch branch) {
        Branch existing = this.BRANCH_REPOSITORY.findById(identifier)
                .orElseThrow(() -> new RuntimeException("Branch with identifier '" + identifier + "' not found"));

        validateGoogleMapsUrl(branch);

        String newMapsUrl = branch.getGoogleMapsUrl();
        String existingMapsUrl = existing.getGoogleMapsUrl();

        if (java.util.Objects.equals(newMapsUrl, existingMapsUrl)) {
            // URL unchanged: keep existing coordinates and Google rating; ignore anything sent in the payload.
            branch.setLatitude(existing.getLatitude());
            branch.setLongitude(existing.getLongitude());
            branch.setGooglePlaceId(existing.getGooglePlaceId());
            branch.setGoogleRating(existing.getGoogleRating());
            branch.setGoogleRatingCount(existing.getGoogleRatingCount());
            branch.setGoogleRatingUpdatedAt(existing.getGoogleRatingUpdatedAt());
            if (branch.getGoogleRating() == null) {
                // Backfill: branch predates the Google rating feature (or last fetch failed).
                resolveGoogleRating(branch);
            }
        } else {
            // URL changed: clear coordinates and Google rating, then resolve fresh ones from the new URL.
            branch.setLatitude(null);
            branch.setLongitude(null);
            branch.setGooglePlaceId(null);
            branch.setGoogleRating(null);
            branch.setGoogleRatingCount(null);
            branch.setGoogleRatingUpdatedAt(null);
            resolveCoordinatesIfMissing(branch);
            resolveGoogleRating(branch);
        }

        branch.setIdentifier(identifier);
        branch.setCreatedAt(existing.getCreatedAt());
        return this.BRANCH_REPOSITORY.save(branch);
    }

    // ================ REFRESH GOOGLE RATING ===================== //
    public Branch refreshGoogleRating(String identifier) {
        Branch branch = this.BRANCH_REPOSITORY.findById(identifier)
                .orElseThrow(() -> new RuntimeException("Branch with identifier '" + identifier + "' not found"));

        if (!StringUtils.hasText(branch.getGoogleMapsUrl())) {
            throw new IllegalArgumentException("Branch has no Google Maps URL.");
        }

        if (branch.getLatitude() == null || branch.getLongitude() == null) {
            resolveCoordinatesIfMissing(branch);
        }
        resolveGoogleRating(branch);

        if (branch.getGoogleRating() == null) {
            this.BRANCH_REPOSITORY.save(branch);
            throw new IllegalStateException("Could not fetch Google rating for this branch's Maps URL.");
        }

        return this.BRANCH_REPOSITORY.save(branch);
    }

    // ================ FIND BY ID ===================== //
    @Override
    public Optional<Branch> findById(String identifier) {
        return this.BRANCH_REPOSITORY.findById(identifier);
    }

    // ================ FIND ALL ===================== //
    @Override
    public List<Branch> findAll() {
        return this.BRANCH_REPOSITORY.findAll();
    }

    // ================ DELETE BY ID ===================== //
    @Override
    public void deleteById(String identifier) {
        if (!this.BRANCH_REPOSITORY.existsById(identifier)) {
            throw new RuntimeException("Branch with identifier '" + identifier + "' not found");
        }
        this.BRANCH_REPOSITORY.deleteById(identifier);
    }

    // ================ EXISTS BY ID ===================== //
    @Override
    public boolean existsById(String identifier) {
        return this.BRANCH_REPOSITORY.existsById(identifier);
    }

    // ================ FIND BY INSTITUTE IDENTIFIER ===================== //
    @Override
    public List<Branch> findByInstituteIdentifier(String instituteIdentifier) {
        return this.BRANCH_REPOSITORY.findByInstituteIdentifier(instituteIdentifier);
    }

    // ================ FIND BY CITY IDENTIFIER ===================== //
    @Override
    public List<Branch> findByCityIdentifier(String cityIdentifier) {
        return this.BRANCH_REPOSITORY.findByCityIdentifier(cityIdentifier);
    }

    // ================ FIND BY INSTITUTE IDENTIFIER AND IS MAIN BRANCH TRUE ===================== //
    @Override
    public Optional<Branch> findByInstituteIdentifierAndIsMainBranchTrue(String instituteIdentifier) {
        return this.BRANCH_REPOSITORY.findByInstituteIdentifierAndIsMainBranchTrue(instituteIdentifier);
    }

    // ================ FIND BY IS ONLINE ONLY TRUE ===================== //
    @Override
    public List<Branch> findByIsOnlineOnlyTrue() {
        return this.BRANCH_REPOSITORY.findByIsOnlineOnlyTrue();
    }

    // ================ FIND BRANCHES WITH UNRESOLVED COORDINATES ===================== //
    @Override
    public List<Branch> findBranchesWithUnresolvedCoordinates() {
        return this.BRANCH_REPOSITORY.findBranchesWithUnresolvedCoordinates();
    }
}
