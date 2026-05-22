package com.classes.Backend.Service.reviews;

import com.classes.Backend.Domain.reviews.InstituteResponse;

import java.util.List;
import java.util.Optional;

public interface InstituteResponseService {
    // ================ CRUD OPERATIONS ===================== //
    InstituteResponse save(InstituteResponse instituteResponse);
    List<InstituteResponse> saveAll(List<InstituteResponse> instituteResponses);
    Optional<InstituteResponse> findById(String identifier);
    List<InstituteResponse> findAll();
    void deleteById(String identifier);
    boolean existsById(String identifier);

    // ================ CUSTOM FINDER METHODS ===================== //
    Optional<InstituteResponse> findByReviewIdentifier(String reviewIdentifier);
    List<InstituteResponse> findByInstituteIdentifier(String instituteIdentifier);
    List<InstituteResponse> findByRespondedBy(String respondedBy);
}
