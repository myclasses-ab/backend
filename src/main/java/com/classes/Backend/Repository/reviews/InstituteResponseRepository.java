package com.classes.Backend.Repository.reviews;

import com.classes.Backend.Domain.reviews.InstituteResponse;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface InstituteResponseRepository extends JpaRepository<InstituteResponse, String> {
    Optional<InstituteResponse> findByReviewIdentifier(String reviewIdentifier);
    List<InstituteResponse> findByInstituteIdentifier(String instituteIdentifier);
    List<InstituteResponse> findByRespondedBy(String respondedBy);
}
