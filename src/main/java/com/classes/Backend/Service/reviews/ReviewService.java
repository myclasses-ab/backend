package com.classes.Backend.Service.reviews;

import com.classes.Backend.Domain.reviews.Review;
import com.classes.Backend.Domain.enums.Standard;
import com.classes.Backend.dto.reviews.RatingSummaryDto;

import java.util.List;
import java.util.Optional;

public interface ReviewService {
    // ================ CRUD OPERATIONS ===================== //
    Review save(Review review);
    List<Review> saveAll(List<Review> reviews);
    Optional<Review> findById(String identifier);
    List<Review> findAll();
    void deleteById(String identifier);
    boolean existsById(String identifier);

    // ================ CUSTOM FINDER METHODS ===================== //
    List<Review> findByInstituteIdentifier(String instituteIdentifier);
    List<Review> findByUserIdentifier(String userIdentifier);
    List<Review> findByStandardWhenEnrolled(Standard standard);
    List<Review> findByIsVerifiedStudentTrue();

    // ================ RATING SUMMARY ===================== //
    RatingSummaryDto getRatingSummary(String instituteIdentifier);
}
