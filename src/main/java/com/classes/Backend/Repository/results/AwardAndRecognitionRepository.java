package com.classes.Backend.Repository.results;

import com.classes.Backend.Domain.results.AwardAndRecognition;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AwardAndRecognitionRepository extends JpaRepository<AwardAndRecognition, String> {
    List<AwardAndRecognition> findByInstituteIdentifier(String instituteIdentifier);
    List<AwardAndRecognition> findByYear(Integer year);
    List<AwardAndRecognition> findByIsVerifiedTrue();
}
