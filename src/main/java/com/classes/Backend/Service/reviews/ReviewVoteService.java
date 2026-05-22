package com.classes.Backend.Service.reviews;

import com.classes.Backend.Domain.reviews.ReviewVote;
import com.classes.Backend.Domain.enums.VoteType;

import java.util.List;
import java.util.Optional;

public interface ReviewVoteService {
    // ================ CRUD OPERATIONS ===================== //
    ReviewVote save(ReviewVote reviewVote);
    List<ReviewVote> saveAll(List<ReviewVote> reviewVotes);
    Optional<ReviewVote> findById(String identifier);
    List<ReviewVote> findAll();
    void deleteById(String identifier);
    boolean existsById(String identifier);

    // ================ CUSTOM FINDER METHODS ===================== //
    List<ReviewVote> findByReviewIdentifier(String reviewIdentifier);
    List<ReviewVote> findByUserIdentifier(String userIdentifier);
    Optional<ReviewVote> findByReviewIdentifierAndUserIdentifier(String reviewIdentifier, String userIdentifier);
    List<ReviewVote> findByVote(VoteType vote);
}
