package com.classes.Backend.Service.institute;

import com.classes.Backend.Domain.institute.InstituteFacility;

import java.util.List;
import java.util.Optional;

public interface InstituteFacilityService {
    // ================ CRUD OPERATIONS ===================== //
    InstituteFacility save(InstituteFacility instituteFacility);
    List<InstituteFacility> saveAll(List<InstituteFacility> instituteFacilities);
    Optional<InstituteFacility> findById(String identifier);
    List<InstituteFacility> findAll();
    void deleteById(String identifier);
    boolean existsById(String identifier);

    // ================ CUSTOM FINDER METHODS ===================== //
    Optional<InstituteFacility> findByInstituteIdentifier(String instituteIdentifier);
}
