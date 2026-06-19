package com.classes.Backend.Service.faculty;

import com.classes.Backend.Domain.faculty.Faculty;
import com.classes.Backend.Repository.faculty.FacultyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class FacultyServiceImpl implements FacultyService {
    private final FacultyRepository FACULTY_REPOSITORY;

    // ================ SAVE FACULTY ===================== //
    @Override
    public Faculty save(Faculty faculty) {
        return this.FACULTY_REPOSITORY.save(faculty);
    }

    // ================ SAVE ALL FACULTIES ===================== //
    @Override
    public List<Faculty> saveAll(List<Faculty> faculties) {
        return this.FACULTY_REPOSITORY.saveAll(faculties);
    }

    // ================ FIND BY ID ===================== //
    @Override
    public Optional<Faculty> findById(String identifier) {
        return this.FACULTY_REPOSITORY.findById(identifier);
    }

    // ================ FIND ALL ===================== //
    @Override
    public List<Faculty> findAll() {
        return this.FACULTY_REPOSITORY.findAll();
    }

    // ================ DELETE BY ID ===================== //
    @Override
    public void deleteById(String identifier) {
        if (!this.FACULTY_REPOSITORY.existsById(identifier)) {
            throw new RuntimeException("Faculty with identifier '" + identifier + "' not found");
        }
        this.FACULTY_REPOSITORY.deleteById(identifier);
    }

    // ================ EXISTS BY ID ===================== //
    @Override
    public boolean existsById(String identifier) {
        return this.FACULTY_REPOSITORY.existsById(identifier);
    }

    // ================ FIND BY INSTITUTE IDENTIFIER ===================== //
    @Override
    public List<Faculty> findByInstituteIdentifier(String instituteIdentifier) {
        return this.FACULTY_REPOSITORY.findByInstituteIdentifier(instituteIdentifier);
    }

    // ================ FIND BY EXPERIENCE YEARS GREATER THAN ===================== //
    @Override
    public List<Faculty> findByExperienceYearsGreaterThan(Integer years) {
        return this.FACULTY_REPOSITORY.findByExperienceYearsGreaterThan(years);
    }
}
