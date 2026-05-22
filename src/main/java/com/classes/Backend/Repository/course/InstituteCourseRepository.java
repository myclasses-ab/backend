package com.classes.Backend.Repository.course;

import com.classes.Backend.Domain.course.InstituteCourse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InstituteCourseRepository extends JpaRepository<InstituteCourse, String> {
    List<InstituteCourse> findByInstituteIdentifier(String instituteIdentifier);
    List<InstituteCourse> findByBranchIdentifier(String branchIdentifier);
    List<InstituteCourse> findByAdmissionOpenTrue();
    List<InstituteCourse> findByIsActiveTrue();
}
