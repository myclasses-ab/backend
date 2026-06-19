package com.classes.Backend.Repository.course;

import com.classes.Backend.Domain.course.InstituteCourse;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InstituteCourseRepository extends JpaRepository<InstituteCourse, String> {
    List<InstituteCourse> findByInstituteIdentifier(String instituteIdentifier);
    List<InstituteCourse> findByBranchIdentifier(String branchIdentifier);
    List<InstituteCourse> findByAdmissionOpenTrue();
}
