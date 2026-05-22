package com.classes.Backend.Controller.reviews;

import com.classes.Backend.Domain.enums.ReviewStatus;
import com.classes.Backend.Domain.enums.Standard;
import com.classes.Backend.Domain.reviews.Review;
import com.classes.Backend.Service.reviews.ReviewServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/reviews")
public class ReviewController {

    private final ReviewServiceImpl REVIEW_SERVICE_IMPL;

    // ================ CREATE REVIEW ===================== //
    @PostMapping
    public ResponseEntity<?> saveReview(@RequestBody Review review) {
        return new ResponseEntity<>(this.REVIEW_SERVICE_IMPL.save(review), HttpStatus.CREATED);
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

    // ================ FIND BY STATUS ===================== //
    @GetMapping("/status/{status}")
    public ResponseEntity<?> findByStatus(@PathVariable ReviewStatus status) {
        return new ResponseEntity<>(this.REVIEW_SERVICE_IMPL.findByStatus(status), HttpStatus.OK);
    }

    // ================ FIND BY INSTITUTE AND STATUS ===================== //
    @GetMapping("/institute/{instituteIdentifier}/status/{status}")
    public ResponseEntity<?> findByInstituteIdentifierAndStatus(@PathVariable String instituteIdentifier, @PathVariable ReviewStatus status) {
        return new ResponseEntity<>(this.REVIEW_SERVICE_IMPL.findByInstituteIdentifierAndStatus(instituteIdentifier, status), HttpStatus.OK);
    }

    // ================ FIND BY STANDARD WHEN ENROLLED ===================== //
    @GetMapping("/standard/{standard}")
    public ResponseEntity<?> findByStandardWhenEnrolled(@PathVariable Standard standard) {
        return new ResponseEntity<>(this.REVIEW_SERVICE_IMPL.findByStandardWhenEnrolled(standard), HttpStatus.OK);
    }

    // ================ FIND RECOMMENDED REVIEWS ===================== //
    @GetMapping("/recommended")
    public ResponseEntity<?> findByWouldRecommendTrue() {
        return new ResponseEntity<>(this.REVIEW_SERVICE_IMPL.findByWouldRecommendTrue(), HttpStatus.OK);
    }

    // ================ FIND VERIFIED STUDENT REVIEWS ===================== //
    @GetMapping("/verified-student")
    public ResponseEntity<?> findByIsVerifiedStudentTrue() {
        return new ResponseEntity<>(this.REVIEW_SERVICE_IMPL.findByIsVerifiedStudentTrue(), HttpStatus.OK);
    }
}
