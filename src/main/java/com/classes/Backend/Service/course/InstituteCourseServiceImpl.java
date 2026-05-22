package com.classes.Backend.Service.course;

import com.classes.Backend.Domain.course.InstituteCourse;
import com.classes.Backend.Repository.course.InstituteCourseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class InstituteCourseServiceImpl implements InstituteCourseService {
    private final InstituteCourseRepository INSTITUTE_COURSE_REPOSITORY;

    // ================ SAVE INSTITUTE COURSE ===================== //
    @Override
    public InstituteCourse save(InstituteCourse instituteCourse) {
        return this.INSTITUTE_COURSE_REPOSITORY.save(instituteCourse);
    }

    // ================ SAVE ALL INSTITUTE COURSES ===================== //
    @Override
    public List<InstituteCourse> saveAll(List<InstituteCourse> instituteCourses) {
        return this.INSTITUTE_COURSE_REPOSITORY.saveAll(instituteCourses);
    }

    // ================ FIND BY ID ===================== //
    @Override
    public Optional<InstituteCourse> findById(String identifier) {
        return this.INSTITUTE_COURSE_REPOSITORY.findById(identifier);
    }

    // ================ FIND ALL ===================== //
    @Override
    public List<InstituteCourse> findAll() {
        return this.INSTITUTE_COURSE_REPOSITORY.findAll();
    }

    // ================ DELETE BY ID ===================== //
    @Override
    public void deleteById(String identifier) {
        if (!this.INSTITUTE_COURSE_REPOSITORY.existsById(identifier)) {
            throw new RuntimeException("InstituteCourse with identifier '" + identifier + "' not found");
        }
        this.INSTITUTE_COURSE_REPOSITORY.deleteById(identifier);
    }

    // ================ EXISTS BY ID ===================== //
    @Override
    public boolean existsById(String identifier) {
        return this.INSTITUTE_COURSE_REPOSITORY.existsById(identifier);
    }

    // ================ FIND BY INSTITUTE IDENTIFIER ===================== //
    @Override
    public List<InstituteCourse> findByInstituteIdentifier(String instituteIdentifier) {
        return this.INSTITUTE_COURSE_REPOSITORY.findByInstituteIdentifier(instituteIdentifier);
    }

    // ================ FIND BY BRANCH IDENTIFIER ===================== //
    @Override
    public List<InstituteCourse> findByBranchIdentifier(String branchIdentifier) {
        return this.INSTITUTE_COURSE_REPOSITORY.findByBranchIdentifier(branchIdentifier);
    }

    // ================ FIND BY ADMISSION OPEN TRUE ===================== //
    @Override
    public List<InstituteCourse> findByAdmissionOpenTrue() {
        return this.INSTITUTE_COURSE_REPOSITORY.findByAdmissionOpenTrue();
    }

    // ================ FIND BY IS ACTIVE TRUE ===================== //
    @Override
    public List<InstituteCourse> findByIsActiveTrue() {
        return this.INSTITUTE_COURSE_REPOSITORY.findByIsActiveTrue();
    }
}
