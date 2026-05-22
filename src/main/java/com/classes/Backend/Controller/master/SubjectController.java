package com.classes.Backend.Controller.master;

import com.classes.Backend.Domain.master.Subject;
import com.classes.Backend.Service.master.SubjectServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/subjects")
public class SubjectController {

    private final SubjectServiceImpl SUBJECT_SERVICE_IMPL;

    // ================ CREATE SUBJECT ===================== //
    @PostMapping
    public ResponseEntity<?> saveSubject(@RequestBody Subject subject) {
        return new ResponseEntity<>(this.SUBJECT_SERVICE_IMPL.save(subject), HttpStatus.CREATED);
    }

    // ================ CREATE ALL SUBJECTS ===================== //
    @PostMapping("/bulk")
    public ResponseEntity<?> saveAllSubjects(@RequestBody List<Subject> subjects) {
        return new ResponseEntity<>(this.SUBJECT_SERVICE_IMPL.saveAll(subjects), HttpStatus.CREATED);
    }

    // ================ GET SUBJECT BY ID ===================== //
    @GetMapping("/{identifier}")
    public ResponseEntity<?> getSubjectById(@PathVariable String identifier) {
        return new ResponseEntity<>(this.SUBJECT_SERVICE_IMPL.findById(identifier), HttpStatus.OK);
    }

    // ================ GET ALL SUBJECTS ===================== //
    @GetMapping
    public ResponseEntity<?> getAllSubjects() {
        List<Subject> allSubjects = this.SUBJECT_SERVICE_IMPL.findAll();
        return new ResponseEntity<>(allSubjects, HttpStatus.OK);
    }

    // ================ DELETE SUBJECT BY ID ===================== //
    @DeleteMapping("/{identifier}")
    public ResponseEntity<?> deleteSubjectById(@PathVariable String identifier) {
        this.SUBJECT_SERVICE_IMPL.deleteById(identifier);
        return new ResponseEntity<>("Subject deleted successfully", HttpStatus.OK);
    }

    // ================ UPDATE SUBJECT BY ID ===================== //
    @PutMapping("/{identifier}")
    public ResponseEntity<?> updateSubjectById(@PathVariable String identifier, @RequestBody Subject subject) {
        if (!this.SUBJECT_SERVICE_IMPL.existsById(identifier)) {
            return new ResponseEntity<>("Subject not found", HttpStatus.NOT_FOUND);
        }
        subject.setIdentifier(identifier);
        return new ResponseEntity<>(this.SUBJECT_SERVICE_IMPL.save(subject), HttpStatus.OK);
    }

    // ================ FIND BY SLUG ===================== //
    @GetMapping("/slug/{slug}")
    public ResponseEntity<?> findBySlug(@PathVariable String slug) {
        return new ResponseEntity<>(this.SUBJECT_SERVICE_IMPL.findBySlug(slug), HttpStatus.OK);
    }

    // ================ FIND BY STREAM IDENTIFIER ===================== //
    @GetMapping("/stream/{streamIdentifier}")
    public ResponseEntity<?> findByStreamIdentifier(@PathVariable String streamIdentifier) {
        return new ResponseEntity<>(this.SUBJECT_SERVICE_IMPL.findByStreamIdentifier(streamIdentifier), HttpStatus.OK);
    }
}
