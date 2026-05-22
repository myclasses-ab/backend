package com.classes.Backend.Service.reviews;

import com.classes.Backend.Domain.reviews.InstituteResponse;
import com.classes.Backend.Repository.reviews.InstituteResponseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class InstituteResponseServiceImpl implements InstituteResponseService {
    private final InstituteResponseRepository INSTITUTE_RESPONSE_REPOSITORY;

    // ================ SAVE INSTITUTE RESPONSE ===================== //
    @Override
    public InstituteResponse save(InstituteResponse instituteResponse) {
        return this.INSTITUTE_RESPONSE_REPOSITORY.save(instituteResponse);
    }

    // ================ SAVE ALL INSTITUTE RESPONSES ===================== //
    @Override
    public List<InstituteResponse> saveAll(List<InstituteResponse> instituteResponses) {
        return this.INSTITUTE_RESPONSE_REPOSITORY.saveAll(instituteResponses);
    }

    // ================ FIND BY ID ===================== //
    @Override
    public Optional<InstituteResponse> findById(String identifier) {
        return this.INSTITUTE_RESPONSE_REPOSITORY.findById(identifier);
    }

    // ================ FIND ALL ===================== //
    @Override
    public List<InstituteResponse> findAll() {
        return this.INSTITUTE_RESPONSE_REPOSITORY.findAll();
    }

    // ================ DELETE BY ID ===================== //
    @Override
    public void deleteById(String identifier) {
        if (!this.INSTITUTE_RESPONSE_REPOSITORY.existsById(identifier)) {
            throw new RuntimeException("InstituteResponse with identifier '" + identifier + "' not found");
        }
        this.INSTITUTE_RESPONSE_REPOSITORY.deleteById(identifier);
    }

    // ================ EXISTS BY ID ===================== //
    @Override
    public boolean existsById(String identifier) {
        return this.INSTITUTE_RESPONSE_REPOSITORY.existsById(identifier);
    }

    // ================ FIND BY REVIEW IDENTIFIER ===================== //
    @Override
    public Optional<InstituteResponse> findByReviewIdentifier(String reviewIdentifier) {
        return this.INSTITUTE_RESPONSE_REPOSITORY.findByReviewIdentifier(reviewIdentifier);
    }

    // ================ FIND BY INSTITUTE IDENTIFIER ===================== //
    @Override
    public List<InstituteResponse> findByInstituteIdentifier(String instituteIdentifier) {
        return this.INSTITUTE_RESPONSE_REPOSITORY.findByInstituteIdentifier(instituteIdentifier);
    }

    // ================ FIND BY RESPONDED BY ===================== //
    @Override
    public List<InstituteResponse> findByRespondedBy(String respondedBy) {
        return this.INSTITUTE_RESPONSE_REPOSITORY.findByRespondedBy(respondedBy);
    }
}
