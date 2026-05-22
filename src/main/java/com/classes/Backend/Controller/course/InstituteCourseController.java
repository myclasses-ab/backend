package com.classes.Backend.Controller.course;

import com.classes.Backend.Domain.course.InstituteCourse;
import com.classes.Backend.Service.course.InstituteCourseServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/institute-courses")
public class InstituteCourseController {

    private final InstituteCourseServiceImpl INSTITUTE_COURSE_SERVICE_IMPL;

    // ================ CREATE INSTITUTE COURSE ===================== //
    @PostMapping
    public ResponseEntity<?> saveInstituteCourse(@RequestBody InstituteCourse instituteCourse) {
        return new ResponseEntity<>(this.INSTITUTE_COURSE_SERVICE_IMPL.save(instituteCourse), HttpStatus.CREATED);
    }

    // ================ CREATE ALL INSTITUTE COURSES ===================== //
    @PostMapping("/bulk")
    public ResponseEntity<?> saveAllInstituteCourses(@RequestBody List<InstituteCourse> instituteCourses) {
        return new ResponseEntity<>(this.INSTITUTE_COURSE_SERVICE_IMPL.saveAll(instituteCourses), HttpStatus.CREATED);
    }

    // ================ GET INSTITUTE COURSE BY ID ===================== //
    @GetMapping("/{identifier}")
    public ResponseEntity<?> getInstituteCourseById(@PathVariable String identifier) {
        return new ResponseEntity<>(this.INSTITUTE_COURSE_SERVICE_IMPL.findById(identifier), HttpStatus.OK);
    }

    // ================ GET ALL INSTITUTE COURSES ===================== //
    @GetMapping
    public ResponseEntity<?> getAllInstituteCourses() {
        List<InstituteCourse> allInstituteCourses = this.INSTITUTE_COURSE_SERVICE_IMPL.findAll();
        return new ResponseEntity<>(allInstituteCourses, HttpStatus.OK);
    }

    // ================ DELETE INSTITUTE COURSE BY ID ===================== //
    @DeleteMapping("/{identifier}")
    public ResponseEntity<?> deleteInstituteCourseById(@PathVariable String identifier) {
        this.INSTITUTE_COURSE_SERVICE_IMPL.deleteById(identifier);
        return new ResponseEntity<>("InstituteCourse deleted successfully", HttpStatus.OK);
    }

    // ================ UPDATE INSTITUTECOURSE BY ID ===================== //
    @PutMapping("/{identifier}")
    public ResponseEntity<?> updateInstituteCourseById(@PathVariable String identifier, @RequestBody InstituteCourse instituteCourse) {
        if (!this.INSTITUTE_COURSE_SERVICE_IMPL.existsById(identifier)) {
            return new ResponseEntity<>("InstituteCourse not found", HttpStatus.NOT_FOUND);
        }
        instituteCourse.setIdentifier(identifier);
        return new ResponseEntity<>(this.INSTITUTE_COURSE_SERVICE_IMPL.save(instituteCourse), HttpStatus.OK);
    }

    // ================ FIND BY INSTITUTE IDENTIFIER ===================== //
    @GetMapping("/institute/{instituteIdentifier}")
    public ResponseEntity<?> findByInstituteIdentifier(@PathVariable String instituteIdentifier) {
        return new ResponseEntity<>(this.INSTITUTE_COURSE_SERVICE_IMPL.findByInstituteIdentifier(instituteIdentifier), HttpStatus.OK);
    }

    // ================ FIND BY BRANCH IDENTIFIER ===================== //
    @GetMapping("/branch/{branchIdentifier}")
    public ResponseEntity<?> findByBranchIdentifier(@PathVariable String branchIdentifier) {
        return new ResponseEntity<>(this.INSTITUTE_COURSE_SERVICE_IMPL.findByBranchIdentifier(branchIdentifier), HttpStatus.OK);
    }

    // ================ FIND BY ADMISSION OPEN TRUE ===================== //
    @GetMapping("/admission-open")
    public ResponseEntity<?> findByAdmissionOpenTrue() {
        return new ResponseEntity<>(this.INSTITUTE_COURSE_SERVICE_IMPL.findByAdmissionOpenTrue(), HttpStatus.OK);
    }

    // ================ FIND ACTIVE INSTITUTE COURSES ===================== //
    @GetMapping("/active")
    public ResponseEntity<?> findByIsActiveTrue() {
        return new ResponseEntity<>(this.INSTITUTE_COURSE_SERVICE_IMPL.findByIsActiveTrue(), HttpStatus.OK);
    }
}
