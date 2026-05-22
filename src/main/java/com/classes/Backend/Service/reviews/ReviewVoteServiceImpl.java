package com.classes.Backend.Service.reviews;

import com.classes.Backend.Domain.reviews.ReviewVote;
import com.classes.Backend.Domain.enums.VoteType;
import com.classes.Backend.Repository.reviews.ReviewVoteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class ReviewVoteServiceImpl implements ReviewVoteService {
    private final ReviewVoteRepository REVIEW_VOTE_REPOSITORY;

    // ================ SAVE REVIEW VOTE ===================== //
    @Override
    public ReviewVote save(ReviewVote reviewVote) {
        return this.REVIEW_VOTE_REPOSITORY.save(reviewVote);
    }

    // ================ SAVE ALL REVIEW VOTES ===================== //
    @Override
    public List<ReviewVote> saveAll(List<ReviewVote> reviewVotes) {
        return this.REVIEW_VOTE_REPOSITORY.saveAll(reviewVotes);
    }

    // ================ FIND BY ID ===================== //
    @Override
    public Optional<ReviewVote> findById(String identifier) {
        return this.REVIEW_VOTE_REPOSITORY.findById(identifier);
    }

    // ================ FIND ALL ===================== //
    @Override
    public List<ReviewVote> findAll() {
        return this.REVIEW_VOTE_REPOSITORY.findAll();
    }

    // ================ DELETE BY ID ===================== //
    @Override
    public void deleteById(String identifier) {
        if (!this.REVIEW_VOTE_REPOSITORY.existsById(identifier)) {
            throw new RuntimeException("ReviewVote with identifier '" + identifier + "' not found");
        }
        this.REVIEW_VOTE_REPOSITORY.deleteById(identifier);
    }

    // ================ EXISTS BY ID ===================== //
    @Override
    public boolean existsById(String identifier) {
        return this.REVIEW_VOTE_REPOSITORY.existsById(identifier);
    }

    // ================ FIND BY REVIEW IDENTIFIER ===================== //
    @Override
    public List<ReviewVote> findByReviewIdentifier(String reviewIdentifier) {
        return this.REVIEW_VOTE_REPOSITORY.findByReviewIdentifier(reviewIdentifier);
    }

    // ================ FIND BY USER IDENTIFIER ===================== //
    @Override
    public List<ReviewVote> findByUserIdentifier(String userIdentifier) {
        return this.REVIEW_VOTE_REPOSITORY.findByUserIdentifier(userIdentifier);
    }

    // ================ FIND BY REVIEW IDENTIFIER AND USER IDENTIFIER ===================== //
    @Override
    public Optional<ReviewVote> findByReviewIdentifierAndUserIdentifier(String reviewIdentifier, String userIdentifier) {
        return this.REVIEW_VOTE_REPOSITORY.findByReviewIdentifierAndUserIdentifier(reviewIdentifier, userIdentifier);
    }

    // ================ FIND BY VOTE ===================== //
    @Override
    public List<ReviewVote> findByVote(VoteType vote) {
        return this.REVIEW_VOTE_REPOSITORY.findByVote(vote);
    }
}
