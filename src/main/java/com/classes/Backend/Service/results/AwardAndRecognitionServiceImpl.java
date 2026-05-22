package com.classes.Backend.Service.results;

import com.classes.Backend.Domain.results.AwardAndRecognition;
import com.classes.Backend.Repository.results.AwardAndRecognitionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class AwardAndRecognitionServiceImpl implements AwardAndRecognitionService {
    private final AwardAndRecognitionRepository AWARD_AND_RECOGNITION_REPOSITORY;

    // ================ SAVE AWARD AND RECOGNITION ===================== //
    @Override
    public AwardAndRecognition save(AwardAndRecognition awardAndRecognition) {
        return this.AWARD_AND_RECOGNITION_REPOSITORY.save(awardAndRecognition);
    }

    // ================ SAVE ALL AWARD AND RECOGNITIONS ===================== //
    @Override
    public List<AwardAndRecognition> saveAll(List<AwardAndRecognition> awardAndRecognitions) {
        return this.AWARD_AND_RECOGNITION_REPOSITORY.saveAll(awardAndRecognitions);
    }

    // ================ FIND BY ID ===================== //
    @Override
    public Optional<AwardAndRecognition> findById(String identifier) {
        return this.AWARD_AND_RECOGNITION_REPOSITORY.findById(identifier);
    }

    // ================ FIND ALL ===================== //
    @Override
    public List<AwardAndRecognition> findAll() {
        return this.AWARD_AND_RECOGNITION_REPOSITORY.findAll();
    }

    // ================ DELETE BY ID ===================== //
    @Override
    public void deleteById(String identifier) {
        if (!this.AWARD_AND_RECOGNITION_REPOSITORY.existsById(identifier)) {
            throw new RuntimeException("AwardAndRecognition with identifier '" + identifier + "' not found");
        }
        this.AWARD_AND_RECOGNITION_REPOSITORY.deleteById(identifier);
    }

    // ================ EXISTS BY ID ===================== //
    @Override
    public boolean existsById(String identifier) {
        return this.AWARD_AND_RECOGNITION_REPOSITORY.existsById(identifier);
    }

    // ================ FIND BY INSTITUTE IDENTIFIER ===================== //
    @Override
    public List<AwardAndRecognition> findByInstituteIdentifier(String instituteIdentifier) {
        return this.AWARD_AND_RECOGNITION_REPOSITORY.findByInstituteIdentifier(instituteIdentifier);
    }

    // ================ FIND BY YEAR ===================== //
    @Override
    public List<AwardAndRecognition> findByYear(Integer year) {
        return this.AWARD_AND_RECOGNITION_REPOSITORY.findByYear(year);
    }

    // ================ FIND BY IS VERIFIED TRUE ===================== //
    @Override
    public List<AwardAndRecognition> findByIsVerifiedTrue() {
        return this.AWARD_AND_RECOGNITION_REPOSITORY.findByIsVerifiedTrue();
    }
}
