package com.classes.Backend.Controller.faculty;

import com.classes.Backend.Domain.faculty.Faculty;
import com.classes.Backend.Service.faculty.FacultyServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/faculty")
public class FacultyController {

    private final FacultyServiceImpl FACULTY_SERVICE_IMPL;

    // ================ CREATE FACULTY ===================== //
    @PostMapping
    public ResponseEntity<?> saveFaculty(@RequestBody Faculty faculty) {
        return new ResponseEntity<>(this.FACULTY_SERVICE_IMPL.save(faculty), HttpStatus.CREATED);
    }

    // ================ CREATE ALL FACULTY ===================== //
    @PostMapping("/bulk")
    public ResponseEntity<?> saveAllFaculty(@RequestBody List<Faculty> facultyList) {
        return new ResponseEntity<>(this.FACULTY_SERVICE_IMPL.saveAll(facultyList), HttpStatus.CREATED);
    }

    // ================ GET FACULTY BY ID ===================== //
    @GetMapping("/{identifier}")
    public ResponseEntity<?> getFacultyById(@PathVariable String identifier) {
        return new ResponseEntity<>(this.FACULTY_SERVICE_IMPL.findById(identifier), HttpStatus.OK);
    }

    // ================ GET ALL FACULTY ===================== //
    @GetMapping
    public ResponseEntity<?> getAllFaculty() {
        List<Faculty> allFaculty = this.FACULTY_SERVICE_IMPL.findAll();
        return new ResponseEntity<>(allFaculty, HttpStatus.OK);
    }

    // ================ DELETE FACULTY BY ID ===================== //
    @DeleteMapping("/{identifier}")
    public ResponseEntity<?> deleteFacultyById(@PathVariable String identifier) {
        this.FACULTY_SERVICE_IMPL.deleteById(identifier);
        return new ResponseEntity<>("Faculty deleted successfully", HttpStatus.OK);
    }

    // ================ UPDATE FACULTY BY ID ===================== //
    @PutMapping("/{identifier}")
    public ResponseEntity<?> updateFacultyById(@PathVariable String identifier, @RequestBody Faculty faculty) {
        if (!this.FACULTY_SERVICE_IMPL.existsById(identifier)) {
            return new ResponseEntity<>("Faculty not found", HttpStatus.NOT_FOUND);
        }
        faculty.setIdentifier(identifier);
        return new ResponseEntity<>(this.FACULTY_SERVICE_IMPL.save(faculty), HttpStatus.OK);
    }

    // ================ FIND BY INSTITUTE IDENTIFIER ===================== //
    @GetMapping("/institute/{instituteIdentifier}")
    public ResponseEntity<?> findByInstituteIdentifier(@PathVariable String instituteIdentifier) {
        return new ResponseEntity<>(this.FACULTY_SERVICE_IMPL.findByInstituteIdentifier(instituteIdentifier), HttpStatus.OK);
    }

    // ================ FIND IIT/IIM BACKGROUND FACULTY ===================== //
    @GetMapping("/iit-iim-background")
    public ResponseEntity<?> findByIitIimBackgroundTrue() {
        return new ResponseEntity<>(this.FACULTY_SERVICE_IMPL.findByIitIimBackgroundTrue(), HttpStatus.OK);
    }

    // ================ FIND NIT BACKGROUND FACULTY ===================== //
    @GetMapping("/nit-background")
    public ResponseEntity<?> findByNitBackgroundTrue() {
        return new ResponseEntity<>(this.FACULTY_SERVICE_IMPL.findByNitBackgroundTrue(), HttpStatus.OK);
    }

    // ================ FIND ACTIVE FACULTY ===================== //
    @GetMapping("/active")
    public ResponseEntity<?> findByIsActiveTrue() {
        return new ResponseEntity<>(this.FACULTY_SERVICE_IMPL.findByIsActiveTrue(), HttpStatus.OK);
    }

    // ================ FIND BY EXPERIENCE YEARS GREATER THAN ===================== //
    @GetMapping("/experience-greater-than/{years}")
    public ResponseEntity<?> findByExperienceYearsGreaterThan(@PathVariable Integer years) {
        return new ResponseEntity<>(this.FACULTY_SERVICE_IMPL.findByExperienceYearsGreaterThan(years), HttpStatus.OK);
    }
}
