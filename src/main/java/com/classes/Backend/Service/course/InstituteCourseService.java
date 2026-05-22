package com.classes.Backend.Service.course;

import com.classes.Backend.Domain.course.InstituteCourse;

import java.util.List;
import java.util.Optional;

public interface InstituteCourseService {
    // ================ CRUD OPERATIONS ===================== //
    InstituteCourse save(InstituteCourse instituteCourse);
    List<InstituteCourse> saveAll(List<InstituteCourse> instituteCourses);
    Optional<InstituteCourse> findById(String identifier);
    List<InstituteCourse> findAll();
    void deleteById(String identifier);
    boolean existsById(String identifier);

    // ================ CUSTOM FINDER METHODS ===================== //
    List<InstituteCourse> findByInstituteIdentifier(String instituteIdentifier);
    List<InstituteCourse> findByBranchIdentifier(String branchIdentifier);
    List<InstituteCourse> findByAdmissionOpenTrue();
    List<InstituteCourse> findByIsActiveTrue();
}
