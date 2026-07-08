package com.classes.Backend.Controller.results;

import com.classes.Backend.Domain.results.Result;
import com.classes.Backend.Service.results.ResultServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/results")
public class ResultController {

    private final ResultServiceImpl RESULT_SERVICE_IMPL;

    // ================ CREATE RESULT ===================== //
    @PostMapping
    public ResponseEntity<?> saveResult(@RequestBody Result result) {
        return new ResponseEntity<>(this.RESULT_SERVICE_IMPL.save(result), HttpStatus.CREATED);
    }

    // ================ CREATE ALL RESULTS ===================== //
    @PostMapping("/bulk")
    public ResponseEntity<?> saveAllResults(@RequestBody List<Result> results) {
        return new ResponseEntity<>(this.RESULT_SERVICE_IMPL.saveAll(results), HttpStatus.CREATED);
    }

    // ================ GET RESULT BY ID ===================== //
    @GetMapping("/{identifier}")
    public ResponseEntity<?> getResultById(@PathVariable String identifier) {
        return new ResponseEntity<>(this.RESULT_SERVICE_IMPL.findById(identifier), HttpStatus.OK);
    }

    // ================ GET ALL RESULTS ===================== //
    @GetMapping
    public ResponseEntity<?> getAllResults() {
        List<Result> allResults = this.RESULT_SERVICE_IMPL.findAll();
        return new ResponseEntity<>(allResults, HttpStatus.OK);
    }

    // ================ DELETE RESULT BY ID ===================== //
    @DeleteMapping("/{identifier}")
    public ResponseEntity<?> deleteResultById(@PathVariable String identifier) {
        this.RESULT_SERVICE_IMPL.deleteById(identifier);
        return new ResponseEntity<>("Result deleted successfully", HttpStatus.OK);
    }

    // ================ UPDATE RESULT BY ID ===================== //
    @PutMapping("/{identifier}")
    public ResponseEntity<?> updateResultById(@PathVariable String identifier, @RequestBody Result result) {
        if (!this.RESULT_SERVICE_IMPL.existsById(identifier)) {
            return new ResponseEntity<>("Result not found", HttpStatus.NOT_FOUND);
        }
        result.setIdentifier(identifier);
        return new ResponseEntity<>(this.RESULT_SERVICE_IMPL.save(result), HttpStatus.OK);
    }

    // ================ FIND BY INSTITUTE IDENTIFIER ===================== //
    @GetMapping("/institute/{instituteIdentifier}")
    public ResponseEntity<?> findByInstituteIdentifier(@PathVariable String instituteIdentifier) {
        return new ResponseEntity<>(this.RESULT_SERVICE_IMPL.findByInstituteIdentifier(instituteIdentifier), HttpStatus.OK);
    }

    // ================ FIND FEATURED RESULTS ===================== //
    @GetMapping("/featured")
    public ResponseEntity<?> findByIsFeaturedTrue() {
        return new ResponseEntity<>(this.RESULT_SERVICE_IMPL.findByIsFeaturedTrue(), HttpStatus.OK);
    }
}
