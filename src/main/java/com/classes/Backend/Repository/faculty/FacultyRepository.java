package com.classes.Backend.Repository.faculty;

import com.classes.Backend.Domain.faculty.Faculty;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FacultyRepository extends JpaRepository<Faculty, String> {
    List<Faculty> findByInstituteIdentifier(String instituteIdentifier);
    List<Faculty> findByIitIimBackgroundTrue();
    List<Faculty> findByNitBackgroundTrue();
    List<Faculty> findByIsActiveTrue();
    List<Faculty> findByExperienceYearsGreaterThan(Integer years);
}
