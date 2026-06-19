package com.classes.Backend.Repository.reviews;

import com.classes.Backend.Domain.reviews.Review;
import com.classes.Backend.Domain.enums.ReviewStatus;
import com.classes.Backend.Domain.enums.Standard;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, String> {
    List<Review> findByInstituteIdentifier(String instituteIdentifier);
    List<Review> findByUserIdentifier(String userIdentifier);
    List<Review> findByStatus(ReviewStatus status);
    List<Review> findByInstituteIdentifierAndStatus(String instituteIdentifier, ReviewStatus status);
    List<Review> findByStandardWhenEnrolled(Standard standard);
    List<Review> findByWouldRecommendTrue();
    List<Review> findByIsVerifiedStudentTrue();
}
