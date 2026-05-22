package com.classes.Backend.Repository.reviews;

import com.classes.Backend.Domain.reviews.InstituteResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InstituteResponseRepository extends JpaRepository<InstituteResponse, String> {
    Optional<InstituteResponse> findByReviewIdentifier(String reviewIdentifier);
    List<InstituteResponse> findByInstituteIdentifier(String instituteIdentifier);
    List<InstituteResponse> findByRespondedBy(String respondedBy);
}
