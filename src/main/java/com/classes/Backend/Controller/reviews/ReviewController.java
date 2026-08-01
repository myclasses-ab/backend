package com.classes.Backend.Controller.reviews;

import com.classes.Backend.Domain.activity.ActivityActionType;
import com.classes.Backend.Domain.activity.ActivityActorType;
import com.classes.Backend.Domain.activity.ActivityEntityType;
import com.classes.Backend.Domain.enums.Standard;
import com.classes.Backend.Domain.reviews.Review;
import com.classes.Backend.Service.activity.ActivityLogActorResolver;
import com.classes.Backend.Service.activity.ActivityLogService;
import com.classes.Backend.Service.activity.ResolvedActor;
import com.classes.Backend.Service.reviews.ReviewServiceImpl;
import com.classes.Backend.dto.activity.ActivityLogRequest;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/reviews")
public class ReviewController {

    private final ReviewServiceImpl REVIEW_SERVICE_IMPL;
    private final ActivityLogService ACTIVITY_LOG_SERVICE;
    private final ActivityLogActorResolver ACTOR_RESOLVER;

    // ================ CREATE REVIEW ===================== //
    @PostMapping
    public ResponseEntity<?> saveReview(@RequestBody Review review, HttpServletRequest request) {
        Review saved = this.REVIEW_SERVICE_IMPL.save(review);

        ResolvedActor actor = ACTOR_RESOLVER.resolve(request);
        if (actor.isAuthenticated()) {
            String actorId = saved.getUserIdentifier() != null ? saved.getUserIdentifier() : actor.getIdentifier();
            ACTIVITY_LOG_SERVICE.log(ActivityLogRequest.builder()
                    .actorType(ActivityActorType.STUDENT)
                    .actorIdentifier(actorId)
                    .actorName(actor.getName())
                    .actionType(ActivityActionType.SUBMITTED_REVIEW)
                    .entityType(ActivityEntityType.REVIEW)
                    .entityIdentifier(saved.getIdentifier())
                    .entityName(saved.getReviewTitle())
                    .instituteIdentifier(saved.getInstituteIdentifier())
                    .description("Submitted a review" + (saved.getReviewTitle() != null ? ": " + saved.getReviewTitle() : ""))
                    .metadata(Map.of(
                            "rating", saved.getOverallRating() != null ? saved.getOverallRating().toString() : null,
                            "isVerifiedStudent", saved.getIsVerifiedStudent()
                    ))
                    .source("FRONTEND")
                    .build());
        }

        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }

    // ================ CREATE ALL REVIEWS ===================== //
    @PostMapping("/bulk")
    public ResponseEntity<?> saveAllReviews(@RequestBody List<Review> reviews) {
        return new ResponseEntity<>(this.REVIEW_SERVICE_IMPL.saveAll(reviews), HttpStatus.CREATED);
    }

    // ================ GET REVIEW BY ID ===================== //
    @GetMapping("/{identifier}")
    public ResponseEntity<?> getReviewById(@PathVariable String identifier) {
        return new ResponseEntity<>(this.REVIEW_SERVICE_IMPL.findById(identifier), HttpStatus.OK);
    }

    // ================ GET ALL REVIEWS ===================== //
    @GetMapping
    public ResponseEntity<?> getAllReviews() {
        List<Review> allReviews = this.REVIEW_SERVICE_IMPL.findAll();
        return new ResponseEntity<>(allReviews, HttpStatus.OK);
    }

    // ================ DELETE REVIEW BY ID ===================== //
    @DeleteMapping("/{identifier}")
    public ResponseEntity<?> deleteReviewById(@PathVariable String identifier) {
        this.REVIEW_SERVICE_IMPL.deleteById(identifier);
        return new ResponseEntity<>("Review deleted successfully", HttpStatus.OK);
    }

    // ================ UPDATE REVIEW BY ID ===================== //
    @PutMapping("/{identifier}")
    public ResponseEntity<?> updateReviewById(@PathVariable String identifier, @RequestBody Review review) {
        if (!this.REVIEW_SERVICE_IMPL.existsById(identifier)) {
            return new ResponseEntity<>("Review not found", HttpStatus.NOT_FOUND);
        }
        review.setIdentifier(identifier);
        return new ResponseEntity<>(this.REVIEW_SERVICE_IMPL.save(review), HttpStatus.OK);
    }

    // ================ FIND BY INSTITUTE IDENTIFIER ===================== //
    @GetMapping("/institute/{instituteIdentifier}")
    public ResponseEntity<?> findByInstituteIdentifier(@PathVariable String instituteIdentifier) {
        return new ResponseEntity<>(this.REVIEW_SERVICE_IMPL.findByInstituteIdentifier(instituteIdentifier), HttpStatus.OK);
    }

    // ================ FIND BY USER IDENTIFIER ===================== //
    @GetMapping("/user/{userIdentifier}")
    public ResponseEntity<?> findByUserIdentifier(@PathVariable String userIdentifier) {
        return new ResponseEntity<>(this.REVIEW_SERVICE_IMPL.findByUserIdentifier(userIdentifier), HttpStatus.OK);
    }

    // ================ FIND BY STANDARD WHEN ENROLLED ===================== //
    @GetMapping("/standard/{standard}")
    public ResponseEntity<?> findByStandardWhenEnrolled(@PathVariable Standard standard) {
        return new ResponseEntity<>(this.REVIEW_SERVICE_IMPL.findByStandardWhenEnrolled(standard), HttpStatus.OK);
    }

    // ================ FIND VERIFIED STUDENT REVIEWS ===================== //
    @GetMapping("/verified-student")
    public ResponseEntity<?> findByIsVerifiedStudentTrue() {
        return new ResponseEntity<>(this.REVIEW_SERVICE_IMPL.findByIsVerifiedStudentTrue(), HttpStatus.OK);
    }

    // ================ GET RATING SUMMARY BY INSTITUTE IDENTIFIER ===================== //
    @GetMapping("/institute/{instituteIdentifier}/rating-summary")
    public ResponseEntity<?> getRatingSummaryByInstituteIdentifier(@PathVariable String instituteIdentifier) {
        return new ResponseEntity<>(this.REVIEW_SERVICE_IMPL.getRatingSummary(instituteIdentifier), HttpStatus.OK);
    }
}
