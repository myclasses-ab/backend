package com.classes.Backend.Repository.faculty;

import com.classes.Backend.Domain.faculty.Faculty;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FacultyRepository extends JpaRepository<Faculty, String> {
    List<Faculty> findByInstituteIdentifier(String instituteIdentifier);
    List<Faculty> findByExperienceYearsGreaterThan(Integer years);
}
