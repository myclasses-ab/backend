package com.classes.Backend.Service.reviews;

import com.classes.Backend.Domain.reviews.Review;
import com.classes.Backend.Domain.enums.ReviewStatus;
import com.classes.Backend.Domain.enums.Standard;
import com.classes.Backend.Repository.reviews.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class ReviewServiceImpl implements ReviewService {
    private final ReviewRepository REVIEW_REPOSITORY;

    // ================ SAVE REVIEW ===================== //
    @Override
    public Review save(Review review) {
        return this.REVIEW_REPOSITORY.save(review);
    }

    // ================ SAVE ALL REVIEWS ===================== //
    @Override
    public List<Review> saveAll(List<Review> reviews) {
        return this.REVIEW_REPOSITORY.saveAll(reviews);
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
    public void deleteById(String identifier) {
        if (!this.REVIEW_REPOSITORY.existsById(identifier)) {
            throw new RuntimeException("Review with identifier '" + identifier + "' not found");
        }
        this.REVIEW_REPOSITORY.deleteById(identifier);
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

    // ================ FIND BY STATUS ===================== //
    @Override
    public List<Review> findByStatus(ReviewStatus status) {
        return this.REVIEW_REPOSITORY.findByStatus(status);
    }

    // ================ FIND BY INSTITUTE IDENTIFIER AND STATUS ===================== //
    @Override
    public List<Review> findByInstituteIdentifierAndStatus(String instituteIdentifier, ReviewStatus status) {
        return this.REVIEW_REPOSITORY.findByInstituteIdentifierAndStatus(instituteIdentifier, status);
    }

    // ================ FIND BY STANDARD WHEN ENROLLED ===================== //
    @Override
    public List<Review> findByStandardWhenEnrolled(Standard standard) {
        return this.REVIEW_REPOSITORY.findByStandardWhenEnrolled(standard);
    }

    // ================ FIND BY WOULD RECOMMEND TRUE ===================== //
    @Override
    public List<Review> findByWouldRecommendTrue() {
        return this.REVIEW_REPOSITORY.findByWouldRecommendTrue();
    }

    // ================ FIND BY IS VERIFIED STUDENT TRUE ===================== //
    @Override
    public List<Review> findByIsVerifiedStudentTrue() {
        return this.REVIEW_REPOSITORY.findByIsVerifiedStudentTrue();
    }
}
