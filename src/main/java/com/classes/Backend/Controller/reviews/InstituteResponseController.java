package com.classes.Backend.Controller.reviews;

import com.classes.Backend.Domain.reviews.InstituteResponse;
import com.classes.Backend.Service.reviews.InstituteResponseServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/institute-responses")
public class InstituteResponseController {

    private final InstituteResponseServiceImpl INSTITUTE_RESPONSE_SERVICE_IMPL;

    // ================ CREATE INSTITUTE RESPONSE ===================== //
    @PostMapping
    public ResponseEntity<?> saveInstituteResponse(@RequestBody InstituteResponse instituteResponse) {
        return new ResponseEntity<>(this.INSTITUTE_RESPONSE_SERVICE_IMPL.save(instituteResponse), HttpStatus.CREATED);
    }

    // ================ CREATE ALL INSTITUTE RESPONSES ===================== //
    @PostMapping("/bulk")
    public ResponseEntity<?> saveAllInstituteResponses(@RequestBody List<InstituteResponse> instituteResponses) {
        return new ResponseEntity<>(this.INSTITUTE_RESPONSE_SERVICE_IMPL.saveAll(instituteResponses), HttpStatus.CREATED);
    }

    // ================ GET INSTITUTE RESPONSE BY ID ===================== //
    @GetMapping("/{identifier}")
    public ResponseEntity<?> getInstituteResponseById(@PathVariable String identifier) {
        return new ResponseEntity<>(this.INSTITUTE_RESPONSE_SERVICE_IMPL.findById(identifier), HttpStatus.OK);
    }

    // ================ GET ALL INSTITUTE RESPONSES ===================== //
    @GetMapping
    public ResponseEntity<?> getAllInstituteResponses() {
        List<InstituteResponse> allResponses = this.INSTITUTE_RESPONSE_SERVICE_IMPL.findAll();
        return new ResponseEntity<>(allResponses, HttpStatus.OK);
    }

    // ================ DELETE INSTITUTE RESPONSE BY ID ===================== //
    @DeleteMapping("/{identifier}")
    public ResponseEntity<?> deleteInstituteResponseById(@PathVariable String identifier) {
        this.INSTITUTE_RESPONSE_SERVICE_IMPL.deleteById(identifier);
        return new ResponseEntity<>("InstituteResponse deleted successfully", HttpStatus.OK);
    }

    // ================ UPDATE INSTITUTERESPONSE BY ID ===================== //
    @PutMapping("/{identifier}")
    public ResponseEntity<?> updateInstituteResponseById(@PathVariable String identifier, @RequestBody InstituteResponse instituteResponse) {
        if (!this.INSTITUTE_RESPONSE_SERVICE_IMPL.existsById(identifier)) {
            return new ResponseEntity<>("InstituteResponse not found", HttpStatus.NOT_FOUND);
        }
        instituteResponse.setIdentifier(identifier);
        return new ResponseEntity<>(this.INSTITUTE_RESPONSE_SERVICE_IMPL.save(instituteResponse), HttpStatus.OK);
    }

    // ================ FIND BY REVIEW IDENTIFIER ===================== //
    @GetMapping("/review/{reviewIdentifier}")
    public ResponseEntity<?> findByReviewIdentifier(@PathVariable String reviewIdentifier) {
        return new ResponseEntity<>(this.INSTITUTE_RESPONSE_SERVICE_IMPL.findByReviewIdentifier(reviewIdentifier), HttpStatus.OK);
    }

    // ================ FIND BY INSTITUTE IDENTIFIER ===================== //
    @GetMapping("/institute/{instituteIdentifier}")
    public ResponseEntity<?> findByInstituteIdentifier(@PathVariable String instituteIdentifier) {
        return new ResponseEntity<>(this.INSTITUTE_RESPONSE_SERVICE_IMPL.findByInstituteIdentifier(instituteIdentifier), HttpStatus.OK);
    }

    // ================ FIND BY RESPONDED BY ===================== //
    @GetMapping("/responded-by/{respondedBy}")
    public ResponseEntity<?> findByRespondedBy(@PathVariable String respondedBy) {
        return new ResponseEntity<>(this.INSTITUTE_RESPONSE_SERVICE_IMPL.findByRespondedBy(respondedBy), HttpStatus.OK);
    }
}
