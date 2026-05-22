package com.classes.Backend.Service.results;

import com.classes.Backend.Domain.results.AwardAndRecognition;

import java.util.List;
import java.util.Optional;

public interface AwardAndRecognitionService {
    // ================ CRUD OPERATIONS ===================== //
    AwardAndRecognition save(AwardAndRecognition awardAndRecognition);
    List<AwardAndRecognition> saveAll(List<AwardAndRecognition> awardAndRecognitions);
    Optional<AwardAndRecognition> findById(String identifier);
    List<AwardAndRecognition> findAll();
    void deleteById(String identifier);
    boolean existsById(String identifier);

    // ================ CUSTOM FINDER METHODS ===================== //
    List<AwardAndRecognition> findByInstituteIdentifier(String instituteIdentifier);
    List<AwardAndRecognition> findByYear(Integer year);
    List<AwardAndRecognition> findByIsVerifiedTrue();
}
