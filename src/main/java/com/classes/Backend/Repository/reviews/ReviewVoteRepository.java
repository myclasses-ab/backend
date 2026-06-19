package com.classes.Backend.Repository.reviews;

import com.classes.Backend.Domain.reviews.ReviewVote;
import com.classes.Backend.Domain.enums.VoteType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ReviewVoteRepository extends JpaRepository<ReviewVote, String> {
    List<ReviewVote> findByReviewIdentifier(String reviewIdentifier);
    List<ReviewVote> findByUserIdentifier(String userIdentifier);
    Optional<ReviewVote> findByReviewIdentifierAndUserIdentifier(String reviewIdentifier, String userIdentifier);
    List<ReviewVote> findByVote(VoteType vote);
}
