package com.classes.Backend.Service.reviews;

import com.classes.Backend.Domain.reviews.Review;
import com.classes.Backend.Domain.enums.Standard;
import com.classes.Backend.Domain.institute.Institute;
import com.classes.Backend.Repository.institute.InstituteRepository;
import com.classes.Backend.Repository.reviews.ReviewRepository;
import com.classes.Backend.dto.reviews.RatingSummaryDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class ReviewServiceImpl implements ReviewService {
    private final ReviewRepository REVIEW_REPOSITORY;
    private final InstituteRepository INSTITUTE_REPOSITORY;

    // ================ SAVE REVIEW ===================== //
    @Override
    @Transactional
    public Review save(Review review) {
        Review saved = this.REVIEW_REPOSITORY.save(review);
        updateInstituteRatingStats(saved.getInstituteIdentifier());
        return saved;
    }

    // ================ SAVE ALL REVIEWS ===================== //
    @Override
    @Transactional
    public List<Review> saveAll(List<Review> reviews) {
        List<Review> saved = this.REVIEW_REPOSITORY.saveAll(reviews);
        saved.stream()
                .map(Review::getInstituteIdentifier)
                .distinct()
                .forEach(this::updateInstituteRatingStats);
        return saved;
    }

    // ================ FIND BY ID ===================== //
    @Override
    public Optional<Review> findById(String identifier) {
        return this.REVIEW_REPOSITORY.findById(identifier);
    }

    // ================ FIND ALL ===================== //
    @Override
    public List<Review> findAll() {
        return this.REVIEW_REPOSITORY.findAll();
    }

    // ================ DELETE BY ID ===================== //
    @Override
    @Transactional
    public void deleteById(String identifier) {
        Review review = this.REVIEW_REPOSITORY.findById(identifier)
                .orElseThrow(() -> new RuntimeException("Review with identifier '" + identifier + "' not found"));
        String instituteIdentifier = review.getInstituteIdentifier();
        this.REVIEW_REPOSITORY.deleteById(identifier);
        updateInstituteRatingStats(instituteIdentifier);
    }

    // ================ EXISTS BY ID ===================== //
    @Override
    public boolean existsById(String identifier) {
        return this.REVIEW_REPOSITORY.existsById(identifier);
    }

    // ================ FIND BY INSTITUTE IDENTIFIER ===================== //
    @Override
    public List<Review> findByInstituteIdentifier(String instituteIdentifier) {
        return this.REVIEW_REPOSITORY.findByInstituteIdentifier(instituteIdentifier);
    }

    // ================ FIND BY USER IDENTIFIER ===================== //
    @Override
    public List<Review> findByUserIdentifier(String userIdentifier) {
        return this.REVIEW_REPOSITORY.findByUserIdentifier(userIdentifier);
    }

    // ================ FIND BY STANDARD WHEN ENROLLED ===================== //
    @Override
    public List<Review> findByStandardWhenEnrolled(Standard standard) {
        return this.REVIEW_REPOSITORY.findByStandardWhenEnrolled(standard);
    }

    // ================ FIND BY IS VERIFIED STUDENT TRUE ===================== //
    @Override
    public List<Review> findByIsVerifiedStudentTrue() {
        return this.REVIEW_REPOSITORY.findByIsVerifiedStudentTrue();
    }

    // ================ RATING SUMMARY ===================== //
    @Override
    public RatingSummaryDto getRatingSummary(String instituteIdentifier) {
        List<Review> reviews = this.REVIEW_REPOSITORY.findByInstituteIdentifier(instituteIdentifier);
        int totalReviews = reviews.size();
        if (totalReviews == 0) {
            return new RatingSummaryDto(BigDecimal.ZERO, 0L);
        }
        BigDecimal sum = reviews.stream()
                .map(Review::getOverallRating)
                .filter(rating -> rating != null)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal averageRating = sum.divide(BigDecimal.valueOf(totalReviews), 2, RoundingMode.HALF_UP);
        return new RatingSummaryDto(averageRating, (long) totalReviews);
    }

    // ================ UPDATE INSTITUTE RATING STATS ===================== //
    private void updateInstituteRatingStats(String instituteIdentifier) {
        if (instituteIdentifier == null || instituteIdentifier.isBlank()) {
            return;
        }

        List<Review> reviews = this.REVIEW_REPOSITORY.findByInstituteIdentifier(instituteIdentifier);

        BigDecimal averageRating;
        int totalReviews = reviews.size();

        if (reviews.isEmpty()) {
            averageRating = BigDecimal.ZERO;
        } else {
            BigDecimal sum = reviews.stream()
                    .map(Review::getOverallRating)
                    .filter(rating -> rating != null)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            averageRating = sum.divide(BigDecimal.valueOf(reviews.size()), 2, RoundingMode.HALF_UP);
        }

        this.INSTITUTE_REPOSITORY.findById(instituteIdentifier).ifPresent(institute -> {
            institute.setAverageRating(averageRating);
            institute.setTotalReviews(totalReviews);
            this.INSTITUTE_REPOSITORY.save(institute);
        });
    }
}
