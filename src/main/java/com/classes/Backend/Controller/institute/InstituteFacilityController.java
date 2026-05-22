package com.classes.Backend.Controller.institute;

import com.classes.Backend.Domain.institute.InstituteFacility;
import com.classes.Backend.Service.institute.InstituteFacilityServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/institute-facilities")
public class InstituteFacilityController {

    private final InstituteFacilityServiceImpl INSTITUTE_FACILITY_SERVICE_IMPL;

    // ================ CREATE INSTITUTE FACILITY ===================== //
    @PostMapping
    public ResponseEntity<?> saveInstituteFacility(@RequestBody InstituteFacility instituteFacility) {
        return new ResponseEntity<>(this.INSTITUTE_FACILITY_SERVICE_IMPL.save(instituteFacility), HttpStatus.CREATED);
    }

    // ================ CREATE ALL INSTITUTE FACILITIES ===================== //
    @PostMapping("/bulk")
    public ResponseEntity<?> saveAllInstituteFacilities(@RequestBody List<InstituteFacility> instituteFacilities) {
        return new ResponseEntity<>(this.INSTITUTE_FACILITY_SERVICE_IMPL.saveAll(instituteFacilities), HttpStatus.CREATED);
    }

    // ================ GET INSTITUTE FACILITY BY ID ===================== //
    @GetMapping("/{identifier}")
    public ResponseEntity<?> getInstituteFacilityById(@PathVariable String identifier) {
        return new ResponseEntity<>(this.INSTITUTE_FACILITY_SERVICE_IMPL.findById(identifier), HttpStatus.OK);
    }

    // ================ GET ALL INSTITUTE FACILITIES ===================== //
    @GetMapping
    public ResponseEntity<?> getAllInstituteFacilities() {
        List<InstituteFacility> allFacilities = this.INSTITUTE_FACILITY_SERVICE_IMPL.findAll();
        return new ResponseEntity<>(allFacilities, HttpStatus.OK);
    }

    // ================ DELETE INSTITUTE FACILITY BY ID ===================== //
    @DeleteMapping("/{identifier}")
    public ResponseEntity<?> deleteInstituteFacilityById(@PathVariable String identifier) {
        this.INSTITUTE_FACILITY_SERVICE_IMPL.deleteById(identifier);
        return new ResponseEntity<>("InstituteFacility deleted successfully", HttpStatus.OK);
    }

    // ================ UPDATE INSTITUTEFACILITY BY ID ===================== //
    @PutMapping("/{identifier}")
    public ResponseEntity<?> updateInstituteFacilityById(@PathVariable String identifier, @RequestBody InstituteFacility instituteFacility) {
        if (!this.INSTITUTE_FACILITY_SERVICE_IMPL.existsById(identifier)) {
            return new ResponseEntity<>("InstituteFacility not found", HttpStatus.NOT_FOUND);
        }
        instituteFacility.setIdentifier(identifier);
        return new ResponseEntity<>(this.INSTITUTE_FACILITY_SERVICE_IMPL.save(instituteFacility), HttpStatus.OK);
    }

    // ================ FIND BY INSTITUTE IDENTIFIER ===================== //
    @GetMapping("/institute/{instituteIdentifier}")
    public ResponseEntity<?> findByInstituteIdentifier(@PathVariable String instituteIdentifier) {
        return new ResponseEntity<>(this.INSTITUTE_FACILITY_SERVICE_IMPL.findByInstituteIdentifier(instituteIdentifier), HttpStatus.OK);
    }
}
