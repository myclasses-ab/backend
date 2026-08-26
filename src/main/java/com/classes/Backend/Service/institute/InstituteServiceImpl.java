package com.classes.Backend.Service.institute;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.classes.Backend.Domain.course.InstituteCourse;
import com.classes.Backend.Domain.enums.InstituteType;
import com.classes.Backend.Domain.enums.MediaEntityType;
import com.classes.Backend.Domain.enums.MediaType;
import com.classes.Backend.Domain.enums.OwnershipType;
import com.classes.Backend.Domain.enums.SubscriptionTier;
import com.classes.Backend.Domain.institute.Branch;
import com.classes.Backend.Domain.institute.Institute;
import com.classes.Backend.Domain.institute.InstituteFacility;
import com.classes.Backend.Domain.master.City;
import com.classes.Backend.Domain.media.Media;
import com.classes.Backend.Repository.course.InstituteCourseRepository;
import com.classes.Backend.Repository.institute.BranchRepository;
import com.classes.Backend.Repository.institute.InstituteFacilityRepository;
import com.classes.Backend.Repository.institute.InstituteRepository;
import com.classes.Backend.Repository.master.CityRepository;
import com.classes.Backend.Repository.media.MediaRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class InstituteServiceImpl implements InstituteService {
    private final InstituteRepository INSTITUTE_REPOSITORY;
    private final InstituteFacilityRepository INSTITUTE_FACILITY_REPOSITORY;
    private final InstituteCourseRepository INSTITUTE_COURSE_REPOSITORY;
    private final MediaRepository MEDIA_REPOSITORY;
    private final BranchRepository BRANCH_REPOSITORY;
    private final CityRepository CITY_REPOSITORY;

    private static final BigDecimal DEFAULT_RADIUS_KM = BigDecimal.valueOf(20);
    private static final BigDecimal MAX_RADIUS_KM = BigDecimal.valueOf(60);

    // ================ SAVE INSTITUTE ===================== //
    @Override
    public Institute save(Institute institute) {
        return this.INSTITUTE_REPOSITORY.save(institute);
    }

    // ================ SAVE ALL INSTITUTES ===================== //
    @Override
    public List<Institute> saveAll(List<Institute> institutes) {
        return this.INSTITUTE_REPOSITORY.saveAll(institutes);
    }

    // ================ FIND BY ID ===================== //
    @Override
    public Optional<Institute> findById(String identifier) {
        Optional<Institute> institute = this.INSTITUTE_REPOSITORY.findById(identifier);
        institute.ifPresent(i -> attachStarredMedia(List.of(i)));
        return institute;
    }

    // ================ FIND ALL ===================== //
    @Override
    public List<Institute> findAll() {
        List<Institute> institutes = this.INSTITUTE_REPOSITORY.findAll();
        attachStarredMedia(institutes);
        return institutes;
    }

    // ================ DELETE BY ID ===================== //
    @Override
    public void deleteById(String identifier) {
        if (!this.INSTITUTE_REPOSITORY.existsById(identifier)) {
            throw new RuntimeException("Institute with identifier '" + identifier + "' not found");
        }
        this.INSTITUTE_REPOSITORY.deleteById(identifier);
    }

    // ================ EXISTS BY ID ===================== //
    @Override
    public boolean existsById(String identifier) {
        return this.INSTITUTE_REPOSITORY.existsById(identifier);
    }

    // ================ FIND BY SLUG ===================== //
    @Override
    public Optional<Institute> findBySlug(String slug) {
        Optional<Institute> institute = this.INSTITUTE_REPOSITORY.findBySlug(slug);
        institute.ifPresent(i -> attachStarredMedia(List.of(i)));
        return institute;
    }

    // ================ FIND BY TYPE ===================== //
    @Override
    public List<Institute> findByType(InstituteType type) {
        return this.INSTITUTE_REPOSITORY.findByType(type);
    }

    // ================ FIND BY OWNERSHIP TYPE ===================== //
    @Override
    public List<Institute> findByOwnershipType(OwnershipType ownershipType) {
        return this.INSTITUTE_REPOSITORY.findByOwnershipType(ownershipType);
    }

    // ================ FIND BY SUBSCRIPTION TIER ===================== //
    @Override
    public List<Institute> findBySubscriptionTier(SubscriptionTier subscriptionTier) {
        return this.INSTITUTE_REPOSITORY.findBySubscriptionTier(subscriptionTier);
    }

    // ================ FIND BY IS VERIFIED TRUE ===================== //
    @Override
    public List<Institute> findByIsVerifiedTrue() {
        return this.INSTITUTE_REPOSITORY.findByIsVerifiedTrue();
    }

    // ================ FIND BY IS FEATURED TRUE ===================== //
    @Override
    public List<Institute> findByIsFeaturedTrue() {
        return this.INSTITUTE_REPOSITORY.findByIsFeaturedTrue();
    }

    // ================ FIND BY IS ACTIVE TRUE ===================== //
    @Override
    public List<Institute> findByIsActiveTrue() {
        return this.INSTITUTE_REPOSITORY.findByIsActiveTrue();
    }

    // ================ FIND BY PARENT INSTITUTE IDENTIFIER ===================== //
    @Override
    public List<Institute> findByParentInstituteIdentifier(String parentInstituteIdentifier) {
        return this.INSTITUTE_REPOSITORY.findByParentInstituteIdentifier(parentInstituteIdentifier);
    }

    // ================ SEARCH INSTITUTES ===================== //
    @Override
    public List<Institute> searchInstitutes(String query, String cityIdentifier, String cityName, BigDecimal minFee, BigDecimal maxFee, BigDecimal minRating, InstituteType type, SubscriptionTier subscriptionTier, Boolean isVerified, Boolean isFeatured, Boolean hasHostel, String sortBy, String sortOrder, BigDecimal userLat, BigDecimal userLng, BigDecimal radiusKm) {
        boolean hasUserReference = userLat != null && userLng != null;
        BigDecimal effectiveRadiusKm = clampRadius(radiusKm);

        // If a city is provided but no user GPS, try to use the city center as the reference point.
        GeoReference reference = null;
        if (hasUserReference) {
            reference = new GeoReference(userLat, userLng, null);
        } else if ((cityIdentifier != null && !cityIdentifier.isBlank()) || (cityName != null && !cityName.isBlank())) {
            reference = resolveCityReference(cityIdentifier, cityName);
        }

        boolean hasDistanceFilter = reference != null;

        List<String> instituteIdentifiersWithinRadius = null;
        List<Branch> matchedBranches = List.of();
        if (hasDistanceFilter) {
            matchedBranches = this.BRANCH_REPOSITORY.findBranchesWithinRadius(
                    reference.latitude(), reference.longitude(), effectiveRadiusKm);
            if (matchedBranches.isEmpty()) {
                return List.of();
            }
            instituteIdentifiersWithinRadius = matchedBranches.stream()
                    .map(Branch::getInstituteIdentifier)
                    .distinct()
                    .toList();
        }

        boolean instituteIdentifiersEmpty = instituteIdentifiersWithinRadius == null || instituteIdentifiersWithinRadius.isEmpty();
        List<String> instituteIdentifiersParam = instituteIdentifiersWithinRadius != null
                ? instituteIdentifiersWithinRadius
                : List.of();

        List<Institute> results = this.INSTITUTE_REPOSITORY.searchInstitutes(
                query, cityIdentifier, cityName, minFee, maxFee, minRating,
                type != null ? type.name() : null,
                isVerified, isFeatured, hasHostel,
                instituteIdentifiersParam,
                instituteIdentifiersEmpty
        );

        if (!results.isEmpty()) {
            List<String> instituteIdentifiers = results.stream()
                    .map(Institute::getIdentifier)
                    .toList();
            List<InstituteFacility> facilities = this.INSTITUTE_FACILITY_REPOSITORY
                    .findByInstituteIdentifierIn(instituteIdentifiers);
            Map<String, InstituteFacility> facilityByInstitute = facilities.stream()
                    .collect(Collectors.toMap(InstituteFacility::getInstituteIdentifier, f -> f, (a, b) -> a));
            results.forEach(institute -> institute.setFacilities(facilityByInstitute.get(institute.getIdentifier())));

            Map<String, List<Branch>> branchesByInstitute = matchedBranches.stream()
                    .collect(Collectors.groupingBy(Branch::getInstituteIdentifier));

            Map<String, BigDecimal> distanceByInstitute = matchedBranches.stream()
                    .collect(Collectors.toMap(
                            Branch::getInstituteIdentifier,
                            branch -> haversineKm(reference.latitude(), reference.longitude(), branch.getLatitude(), branch.getLongitude()),
                            (a, b) -> a.compareTo(b) <= 0 ? a : b));

            boolean hasSearchCriteria = (query != null && !query.trim().isEmpty())
                    || (cityIdentifier != null && !cityIdentifier.isEmpty())
                    || (cityName != null && !cityName.trim().isEmpty());

            if (hasSearchCriteria) {
                List<String> matchedBranchIdentifiers = matchedBranches.stream()
                        .map(Branch::getIdentifier)
                        .toList();
                List<InstituteCourse> matchingCourses;
                if (hasDistanceFilter && !matchedBranchIdentifiers.isEmpty()) {
                    matchingCourses = this.INSTITUTE_COURSE_REPOSITORY
                            .findByInstituteIdentifierInAndBranchIdentifierIn(instituteIdentifiers, matchedBranchIdentifiers);
                } else {
                    matchingCourses = this.INSTITUTE_COURSE_REPOSITORY
                            .findMatchingCourses(instituteIdentifiers, query, cityIdentifier, cityName);
                }
                Map<String, List<InstituteCourse>> matchingCoursesByInstitute = matchingCourses.stream()
                        .collect(Collectors.groupingBy(InstituteCourse::getInstituteIdentifier));
                results.forEach(institute -> institute.setMatchingCourses(matchingCoursesByInstitute.get(institute.getIdentifier())));
            }

            if (hasDistanceFilter) {
                results.forEach(institute -> {
                    institute.setDistanceKm(distanceByInstitute.get(institute.getIdentifier()));
                    institute.setMatchedBranches(branchesByInstitute.getOrDefault(institute.getIdentifier(), List.of()));
                });
            }
        }

        attachStarredMedia(results);

        results.sort((a, b) -> {
            int comparison = 0;
            switch (sortBy != null ? sortBy : "relevance") {
                case "distance":
                    comparison = compareBigDecimal(a.getDistanceKm(), b.getDistanceKm());
                    break;
                case "rating":
                    comparison = compareBigDecimal(a.getAverageRating(), b.getAverageRating());
                    break;
                case "name":
                    comparison = compareStrings(a.getName(), b.getName());
                    break;
                case "popularity":
                    comparison = compareIntegers(a.getTotalReviews(), b.getTotalReviews());
                    break;
                case "experience":
                    comparison = compareIntegers(a.getYearsOfExperience(), b.getYearsOfExperience());
                    break;
                case "fees":
                    comparison = 0;
                    break;
                default:
                    if (hasDistanceFilter) {
                        comparison = compareBigDecimal(a.getDistanceKm(), b.getDistanceKm());
                        if (comparison != 0) {
                            break;
                        }
                    }
                    if (Boolean.TRUE.equals(a.getIsFeatured()) != Boolean.TRUE.equals(b.getIsFeatured())) {
                        return Boolean.TRUE.equals(a.getIsFeatured()) ? -1 : 1;
                    }
                    if (Boolean.TRUE.equals(a.getIsVerified()) != Boolean.TRUE.equals(b.getIsVerified())) {
                        return Boolean.TRUE.equals(a.getIsVerified()) ? -1 : 1;
                    }
                    comparison = compareBigDecimal(a.getAverageRating(), b.getAverageRating());
                    break;
            }
            return "asc".equalsIgnoreCase(sortOrder) ? -comparison : comparison;
        });

        return results;
    }

    private record GeoReference(BigDecimal latitude, BigDecimal longitude, String label) {
    }

    private GeoReference resolveCityReference(String cityIdentifier, String cityName) {
        Optional<City> city = Optional.empty();
        if (cityIdentifier != null && !cityIdentifier.isBlank()) {
            city = this.CITY_REPOSITORY.findById(cityIdentifier);
        }
        if (city.isEmpty() && cityName != null && !cityName.isBlank()) {
            city = this.CITY_REPOSITORY.findAll().stream()
                    .filter(c -> c.getName() != null && c.getName().equalsIgnoreCase(cityName.trim()))
                    .findFirst();
        }
        return city.filter(c -> c.getLatitude() != null && c.getLongitude() != null)
                .map(c -> new GeoReference(c.getLatitude(), c.getLongitude(), c.getName()))
                .orElse(null);
    }

    private BigDecimal clampRadius(BigDecimal radiusKm) {
        if (radiusKm == null || radiusKm.compareTo(BigDecimal.ZERO) <= 0) {
            return DEFAULT_RADIUS_KM;
        }
        if (radiusKm.compareTo(MAX_RADIUS_KM) > 0) {
            return MAX_RADIUS_KM;
        }
        return radiusKm;
    }

    private BigDecimal haversineKm(BigDecimal lat1, BigDecimal lng1, BigDecimal lat2, BigDecimal lng2) {
        double r = 6371;
        double dLat = Math.toRadians(lat2.doubleValue() - lat1.doubleValue());
        double dLng = Math.toRadians(lng2.doubleValue() - lng1.doubleValue());
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1.doubleValue())) * Math.cos(Math.toRadians(lat2.doubleValue()))
                * Math.sin(dLng / 2) * Math.sin(dLng / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return BigDecimal.valueOf(r * c);
    }

    private int compareBigDecimal(BigDecimal a, BigDecimal b) {
        if (a == null && b == null) return 0;
        if (a == null) return -1;
        if (b == null) return 1;
        return a.compareTo(b);
    }

    private int compareStrings(String a, String b) {
        if (a == null && b == null) return 0;
        if (a == null) return -1;
        if (b == null) return 1;
        return a.compareToIgnoreCase(b);
    }

    private int compareIntegers(Integer a, Integer b) {
        if (a == null && b == null) return 0;
        if (a == null) return -1;
        if (b == null) return 1;
        return a.compareTo(b);
    }

    // ================ ATTACH STARRED MEDIA URLS ===================== //
    private void attachStarredMedia(List<Institute> institutes) {
        if (institutes == null || institutes.isEmpty()) {
            return;
        }

        List<String> instituteIdentifiers = institutes.stream()
                .map(Institute::getIdentifier)
                .toList();

        List<Media> starredMedia = this.MEDIA_REPOSITORY.findStarredImagesByInstituteIdentifiers(
                instituteIdentifiers, MediaEntityType.INSTITUTE, MediaType.IMAGE);

        Map<String, List<String>> starredUrlsByInstitute = starredMedia.stream()
                .collect(Collectors.groupingBy(
                        Media::getInstituteIdentifier,
                        Collectors.mapping(Media::getUrl, Collectors.toList())
                ));

        institutes.forEach(institute -> {
            List<String> urls = starredUrlsByInstitute.getOrDefault(institute.getIdentifier(), List.of());
            // Limit to first 3 just in case
            institute.setStarredMediaUrls(urls.size() > 3 ? urls.subList(0, 3) : urls);
        });
    }
}
