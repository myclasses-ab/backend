package com.classes.Backend.Controller.reviews;

import com.classes.Backend.Domain.enums.VoteType;
import com.classes.Backend.Domain.reviews.ReviewVote;
import com.classes.Backend.Service.reviews.ReviewVoteServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/review-votes")
public class ReviewVoteController {

    private final ReviewVoteServiceImpl REVIEW_VOTE_SERVICE_IMPL;

    // ================ CREATE REVIEW VOTE ===================== //
    @PostMapping
    public ResponseEntity<?> saveReviewVote(@RequestBody ReviewVote reviewVote) {
        return new ResponseEntity<>(this.REVIEW_VOTE_SERVICE_IMPL.save(reviewVote), HttpStatus.CREATED);
    }

    // ================ CREATE ALL REVIEW VOTES ===================== //
    @PostMapping("/bulk")
    public ResponseEntity<?> saveAllReviewVotes(@RequestBody List<ReviewVote> reviewVotes) {
        return new ResponseEntity<>(this.REVIEW_VOTE_SERVICE_IMPL.saveAll(reviewVotes), HttpStatus.CREATED);
    }

    // ================ GET REVIEW VOTE BY ID ===================== //
    @GetMapping("/{identifier}")
    public ResponseEntity<?> getReviewVoteById(@PathVariable String identifier) {
        return new ResponseEntity<>(this.REVIEW_VOTE_SERVICE_IMPL.findById(identifier), HttpStatus.OK);
    }

    // ================ GET ALL REVIEW VOTES ===================== //
    @GetMapping
    public ResponseEntity<?> getAllReviewVotes() {
        List<ReviewVote> allReviewVotes = this.REVIEW_VOTE_SERVICE_IMPL.findAll();
        return new ResponseEntity<>(allReviewVotes, HttpStatus.OK);
    }

    // ================ DELETE REVIEW VOTE BY ID ===================== //
    @DeleteMapping("/{identifier}")
    public ResponseEntity<?> deleteReviewVoteById(@PathVariable String identifier) {
        this.REVIEW_VOTE_SERVICE_IMPL.deleteById(identifier);
        return new ResponseEntity<>("ReviewVote deleted successfully", HttpStatus.OK);
    }

    // ================ UPDATE REVIEW VOTE BY ID ===================== //
    @PutMapping("/{identifier}")
    public ResponseEntity<?> updateReviewVoteById(@PathVariable String identifier, @RequestBody ReviewVote reviewVote) {
        if (!this.REVIEW_VOTE_SERVICE_IMPL.existsById(identifier)) {
            return new ResponseEntity<>("ReviewVote not found", HttpStatus.NOT_FOUND);
        }
        reviewVote.setIdentifier(identifier);
        return new ResponseEntity<>(this.REVIEW_VOTE_SERVICE_IMPL.save(reviewVote), HttpStatus.OK);
    }

    // ================ FIND BY REVIEW IDENTIFIER ===================== //
    @GetMapping("/review/{reviewIdentifier}")
    public ResponseEntity<?> findByReviewIdentifier(@PathVariable String reviewIdentifier) {
        return new ResponseEntity<>(this.REVIEW_VOTE_SERVICE_IMPL.findByReviewIdentifier(reviewIdentifier), HttpStatus.OK);
    }

    // ================ FIND BY USER IDENTIFIER ===================== //
    @GetMapping("/user/{userIdentifier}")
    public ResponseEntity<?> findByUserIdentifier(@PathVariable String userIdentifier) {
        return new ResponseEntity<>(this.REVIEW_VOTE_SERVICE_IMPL.findByUserIdentifier(userIdentifier), HttpStatus.OK);
    }

    // ================ FIND BY REVIEW AND USER ===================== //
    @GetMapping("/review/{reviewIdentifier}/user/{userIdentifier}")
    public ResponseEntity<?> findByReviewIdentifierAndUserIdentifier(@PathVariable String reviewIdentifier, @PathVariable String userIdentifier) {
        return new ResponseEntity<>(this.REVIEW_VOTE_SERVICE_IMPL.findByReviewIdentifierAndUserIdentifier(reviewIdentifier, userIdentifier), HttpStatus.OK);
    }

    // ================ FIND BY VOTE TYPE ===================== //
    @GetMapping("/vote/{vote}")
    public ResponseEntity<?> findByVote(@PathVariable VoteType vote) {
        return new ResponseEntity<>(this.REVIEW_VOTE_SERVICE_IMPL.findByVote(vote), HttpStatus.OK);
    }
}
