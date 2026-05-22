package com.classes.Backend.Service.faculty;

import com.classes.Backend.Domain.faculty.Faculty;

import java.util.List;
import java.util.Optional;

public interface FacultyService {
    // ================ CRUD OPERATIONS ===================== //
    Faculty save(Faculty faculty);
    List<Faculty> saveAll(List<Faculty> faculties);
    Optional<Faculty> findById(String identifier);
    List<Faculty> findAll();
    void deleteById(String identifier);
    boolean existsById(String identifier);

    // ================ CUSTOM FINDER METHODS ===================== //
    List<Faculty> findByInstituteIdentifier(String instituteIdentifier);
    List<Faculty> findByIitIimBackgroundTrue();
    List<Faculty> findByNitBackgroundTrue();
    List<Faculty> findByIsActiveTrue();
    List<Faculty> findByExperienceYearsGreaterThan(Integer years);
}
