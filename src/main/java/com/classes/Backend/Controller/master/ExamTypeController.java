package com.classes.Backend.Controller.master;

import com.classes.Backend.Domain.master.ExamType;
import com.classes.Backend.Service.master.ExamTypeServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/exam-types")
public class ExamTypeController {

    private final ExamTypeServiceImpl EXAM_TYPE_SERVICE_IMPL;

    // ================ CREATE EXAM TYPE ===================== //
    @PostMapping
    public ResponseEntity<?> saveExamType(@RequestBody ExamType examType) {
        return new ResponseEntity<>(this.EXAM_TYPE_SERVICE_IMPL.save(examType), HttpStatus.CREATED);
    }

    // ================ CREATE ALL EXAM TYPES ===================== //
    @PostMapping("/bulk")
    public ResponseEntity<?> saveAllExamTypes(@RequestBody List<ExamType> examTypes) {
        return new ResponseEntity<>(this.EXAM_TYPE_SERVICE_IMPL.saveAll(examTypes), HttpStatus.CREATED);
    }

    // ================ GET EXAM TYPE BY ID ===================== //
    @GetMapping("/{identifier}")
    public ResponseEntity<?> getExamTypeById(@PathVariable String identifier) {
        return new ResponseEntity<>(this.EXAM_TYPE_SERVICE_IMPL.findById(identifier), HttpStatus.OK);
    }

    // ================ GET ALL EXAM TYPES ===================== //
    @GetMapping
    public ResponseEntity<?> getAllExamTypes() {
        List<ExamType> allExamTypes = this.EXAM_TYPE_SERVICE_IMPL.findAll();
        return new ResponseEntity<>(allExamTypes, HttpStatus.OK);
    }

    // ================ DELETE EXAM TYPE BY ID ===================== //
    @DeleteMapping("/{identifier}")
    public ResponseEntity<?> deleteExamTypeById(@PathVariable String identifier) {
        this.EXAM_TYPE_SERVICE_IMPL.deleteById(identifier);
        return new ResponseEntity<>("ExamType deleted successfully", HttpStatus.OK);
    }

    // ================ UPDATE EXAMTYPE BY ID ===================== //
    @PutMapping("/{identifier}")
    public ResponseEntity<?> updateExamTypeById(@PathVariable String identifier, @RequestBody ExamType examType) {
        if (!this.EXAM_TYPE_SERVICE_IMPL.existsById(identifier)) {
            return new ResponseEntity<>("ExamType not found", HttpStatus.NOT_FOUND);
        }
        examType.setIdentifier(identifier);
        return new ResponseEntity<>(this.EXAM_TYPE_SERVICE_IMPL.save(examType), HttpStatus.OK);
    }

    // ================ FIND BY SLUG ===================== //
    @GetMapping("/slug/{slug}")
    public ResponseEntity<?> findBySlug(@PathVariable String slug) {
        return new ResponseEntity<>(this.EXAM_TYPE_SERVICE_IMPL.findBySlug(slug), HttpStatus.OK);
    }

    // ================ FIND BY STREAM IDENTIFIER ===================== //
    @GetMapping("/stream/{streamIdentifier}")
    public ResponseEntity<?> findByStreamIdentifier(@PathVariable String streamIdentifier) {
        return new ResponseEntity<>(this.EXAM_TYPE_SERVICE_IMPL.findByStreamIdentifier(streamIdentifier), HttpStatus.OK);
    }
}
