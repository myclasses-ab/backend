package com.classes.Backend.Repository.course;

import com.classes.Backend.Domain.course.InstituteCourse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface InstituteCourseRepository extends JpaRepository<InstituteCourse, String> {
    List<InstituteCourse> findByInstituteIdentifier(String instituteIdentifier);

    @Query(value = """
            SELECT * FROM institute_courses
            WHERE institute_identifier IN :instituteIdentifiers
              AND :query IS NOT NULL
              AND custom_name ILIKE CONCAT('%', :query, '%')
            """, nativeQuery = true)
    List<InstituteCourse> findMatchingCourses(
            @Param("instituteIdentifiers") List<String> instituteIdentifiers,
            @Param("query") String query
    );

    List<InstituteCourse> findByBranchIdentifier(String branchIdentifier);
    List<InstituteCourse> findByAdmissionOpenTrue();
}
