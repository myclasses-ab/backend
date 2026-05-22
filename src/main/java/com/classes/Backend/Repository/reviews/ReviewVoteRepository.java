package com.classes.Backend.Repository.reviews;

import com.classes.Backend.Domain.reviews.ReviewVote;
import com.classes.Backend.Domain.enums.VoteType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReviewVoteRepository extends JpaRepository<ReviewVote, String> {
    List<ReviewVote> findByReviewIdentifier(String reviewIdentifier);
    List<ReviewVote> findByUserIdentifier(String userIdentifier);
    Optional<ReviewVote> findByReviewIdentifierAndUserIdentifier(String reviewIdentifier, String userIdentifier);
    List<ReviewVote> findByVote(VoteType vote);
}
