package com.classes.Backend.Controller.results;

import com.classes.Backend.Domain.results.AwardAndRecognition;
import com.classes.Backend.Service.results.AwardAndRecognitionServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/awards-and-recognitions")
public class AwardAndRecognitionController {

    private final AwardAndRecognitionServiceImpl AWARD_AND_RECOGNITION_SERVICE_IMPL;

    // ================ CREATE AWARD AND RECOGNITION ===================== //
    @PostMapping
    public ResponseEntity<?> saveAwardAndRecognition(@RequestBody AwardAndRecognition awardAndRecognition) {
        return new ResponseEntity<>(this.AWARD_AND_RECOGNITION_SERVICE_IMPL.save(awardAndRecognition), HttpStatus.CREATED);
    }

    // ================ CREATE ALL AWARDS AND RECOGNITIONS ===================== //
    @PostMapping("/bulk")
    public ResponseEntity<?> saveAllAwardsAndRecognitions(@RequestBody List<AwardAndRecognition> awardsAndRecognitions) {
        return new ResponseEntity<>(this.AWARD_AND_RECOGNITION_SERVICE_IMPL.saveAll(awardsAndRecognitions), HttpStatus.CREATED);
    }

    // ================ GET AWARD AND RECOGNITION BY ID ===================== //
    @GetMapping("/{identifier}")
    public ResponseEntity<?> getAwardAndRecognitionById(@PathVariable String identifier) {
        return new ResponseEntity<>(this.AWARD_AND_RECOGNITION_SERVICE_IMPL.findById(identifier), HttpStatus.OK);
    }

    // ================ GET ALL AWARDS AND RECOGNITIONS ===================== //
    @GetMapping
    public ResponseEntity<?> getAllAwardsAndRecognitions() {
        List<AwardAndRecognition> allAwards = this.AWARD_AND_RECOGNITION_SERVICE_IMPL.findAll();
        return new ResponseEntity<>(allAwards, HttpStatus.OK);
    }

    // ================ DELETE AWARD AND RECOGNITION BY ID ===================== //
    @DeleteMapping("/{identifier}")
    public ResponseEntity<?> deleteAwardAndRecognitionById(@PathVariable String identifier) {
        this.AWARD_AND_RECOGNITION_SERVICE_IMPL.deleteById(identifier);
        return new ResponseEntity<>("AwardAndRecognition deleted successfully", HttpStatus.OK);
    }

    // ================ UPDATE AWARDANDRECOGNITION BY ID ===================== //
    @PutMapping("/{identifier}")
    public ResponseEntity<?> updateAwardAndRecognitionById(@PathVariable String identifier, @RequestBody AwardAndRecognition awardAndRecognition) {
        if (!this.AWARD_AND_RECOGNITION_SERVICE_IMPL.existsById(identifier)) {
            return new ResponseEntity<>("AwardAndRecognition not found", HttpStatus.NOT_FOUND);
        }
        awardAndRecognition.setIdentifier(identifier);
        return new ResponseEntity<>(this.AWARD_AND_RECOGNITION_SERVICE_IMPL.save(awardAndRecognition), HttpStatus.OK);
    }

    // ================ FIND BY INSTITUTE IDENTIFIER ===================== //
    @GetMapping("/institute/{instituteIdentifier}")
    public ResponseEntity<?> findByInstituteIdentifier(@PathVariable String instituteIdentifier) {
        return new ResponseEntity<>(this.AWARD_AND_RECOGNITION_SERVICE_IMPL.findByInstituteIdentifier(instituteIdentifier), HttpStatus.OK);
    }

    // ================ FIND BY YEAR ===================== //
    @GetMapping("/year/{year}")
    public ResponseEntity<?> findByYear(@PathVariable Integer year) {
        return new ResponseEntity<>(this.AWARD_AND_RECOGNITION_SERVICE_IMPL.findByYear(year), HttpStatus.OK);
    }

    // ================ FIND VERIFIED AWARDS ===================== //
    @GetMapping("/verified")
    public ResponseEntity<?> findByIsVerifiedTrue() {
        return new ResponseEntity<>(this.AWARD_AND_RECOGNITION_SERVICE_IMPL.findByIsVerifiedTrue(), HttpStatus.OK);
    }
}
