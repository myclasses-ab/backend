package com.classes.Backend.Repository.course;

import com.classes.Backend.Domain.course.InstituteCourse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface InstituteCourseRepository extends JpaRepository<InstituteCourse, String> {
    List<InstituteCourse> findByInstituteIdentifier(String instituteIdentifier);

    @Query(value = """
            SELECT DISTINCT ic.* FROM institute_courses ic
            INNER JOIN branches b ON b.identifier = ic.branch_identifier
            WHERE ic.institute_identifier IN :instituteIdentifiers
              AND (:query IS NULL OR ic.custom_name ILIKE CONCAT('%', :query, '%'))
              AND (:cityIdentifier IS NULL OR b.city_identifier = :cityIdentifier)
              AND (:cityName IS NULL OR b.city_name ILIKE CONCAT('%', :cityName, '%'))
            """, nativeQuery = true)
    List<InstituteCourse> findMatchingCourses(
            @Param("instituteIdentifiers") List<String> instituteIdentifiers,
            @Param("query") String query,
            @Param("cityIdentifier") String cityIdentifier,
            @Param("cityName") String cityName
    );

    List<InstituteCourse> findByInstituteIdentifierInAndBranchIdentifierIn(
            List<String> instituteIdentifiers,
            List<String> branchIdentifiers
    );

    List<InstituteCourse> findByBranchIdentifier(String branchIdentifier);
    List<InstituteCourse> findByAdmissionOpenTrue();
}
