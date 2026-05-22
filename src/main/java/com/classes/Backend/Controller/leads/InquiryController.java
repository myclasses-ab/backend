package com.classes.Backend.Controller.leads;

import com.classes.Backend.Domain.enums.InquirySource;
import com.classes.Backend.Domain.enums.InquiryStatus;
import com.classes.Backend.Domain.leads.Inquiry;
import com.classes.Backend.Service.leads.InquiryServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/inquiries")
public class InquiryController {

    private final InquiryServiceImpl INQUIRY_SERVICE_IMPL;

    // ================ CREATE INQUIRY ===================== //
    @PostMapping
    public ResponseEntity<?> saveInquiry(@RequestBody Inquiry inquiry) {
        return new ResponseEntity<>(this.INQUIRY_SERVICE_IMPL.save(inquiry), HttpStatus.CREATED);
    }

    // ================ CREATE ALL INQUIRIES ===================== //
    @PostMapping("/bulk")
    public ResponseEntity<?> saveAllInquiries(@RequestBody List<Inquiry> inquiries) {
        return new ResponseEntity<>(this.INQUIRY_SERVICE_IMPL.saveAll(inquiries), HttpStatus.CREATED);
    }

    // ================ GET INQUIRY BY ID ===================== //
    @GetMapping("/{identifier}")
    public ResponseEntity<?> getInquiryById(@PathVariable String identifier) {
        return new ResponseEntity<>(this.INQUIRY_SERVICE_IMPL.findById(identifier), HttpStatus.OK);
    }

    // ================ GET ALL INQUIRIES ===================== //
    @GetMapping
    public ResponseEntity<?> getAllInquiries() {
        List<Inquiry> allInquiries = this.INQUIRY_SERVICE_IMPL.findAll();
        return new ResponseEntity<>(allInquiries, HttpStatus.OK);
    }

    // ================ DELETE INQUIRY BY ID ===================== //
    @DeleteMapping("/{identifier}")
    public ResponseEntity<?> deleteInquiryById(@PathVariable String identifier) {
        this.INQUIRY_SERVICE_IMPL.deleteById(identifier);
        return new ResponseEntity<>("Inquiry deleted successfully", HttpStatus.OK);
    }

    // ================ UPDATE INQUIRYSOURCE BY ID ===================== //
    @PutMapping("/{identifier}")
    public ResponseEntity<?> updateInquiryById(@PathVariable String identifier, @RequestBody Inquiry inquiry) {
        if (!this.INQUIRY_SERVICE_IMPL.existsById(identifier)) {
            return new ResponseEntity<>("InquirySource not found", HttpStatus.NOT_FOUND);
        }
        inquiry.setIdentifier(identifier);
        return new ResponseEntity<>(this.INQUIRY_SERVICE_IMPL.save(inquiry), HttpStatus.OK);
    }

    // ================ FIND BY INSTITUTE IDENTIFIER ===================== //
    @GetMapping("/institute/{instituteIdentifier}")
    public ResponseEntity<?> findByInstituteIdentifier(@PathVariable String instituteIdentifier) {
        return new ResponseEntity<>(this.INQUIRY_SERVICE_IMPL.findByInstituteIdentifier(instituteIdentifier), HttpStatus.OK);
    }

    // ================ FIND BY BRANCH IDENTIFIER ===================== //
    @GetMapping("/branch/{branchIdentifier}")
    public ResponseEntity<?> findByBranchIdentifier(@PathVariable String branchIdentifier) {
        return new ResponseEntity<>(this.INQUIRY_SERVICE_IMPL.findByBranchIdentifier(branchIdentifier), HttpStatus.OK);
    }

    // ================ FIND BY COURSE IDENTIFIER ===================== //
    @GetMapping("/course/{courseIdentifier}")
    public ResponseEntity<?> findByCourseIdentifier(@PathVariable String courseIdentifier) {
        return new ResponseEntity<>(this.INQUIRY_SERVICE_IMPL.findByCourseIdentifier(courseIdentifier), HttpStatus.OK);
    }

    // ================ FIND BY USER IDENTIFIER ===================== //
    @GetMapping("/user/{userIdentifier}")
    public ResponseEntity<?> findByUserIdentifier(@PathVariable String userIdentifier) {
        return new ResponseEntity<>(this.INQUIRY_SERVICE_IMPL.findByUserIdentifier(userIdentifier), HttpStatus.OK);
    }

    // ================ FIND BY STATUS ===================== //
    @GetMapping("/status/{status}")
    public ResponseEntity<?> findByStatus(@PathVariable InquiryStatus status) {
        return new ResponseEntity<>(this.INQUIRY_SERVICE_IMPL.findByStatus(status), HttpStatus.OK);
    }

    // ================ FIND BY SOURCE ===================== //
    @GetMapping("/source/{source}")
    public ResponseEntity<?> findBySource(@PathVariable InquirySource source) {
        return new ResponseEntity<>(this.INQUIRY_SERVICE_IMPL.findBySource(source), HttpStatus.OK);
    }

    // ================ FIND BY ASSIGNED TO ===================== //
    @GetMapping("/assigned-to/{assignedTo}")
    public ResponseEntity<?> findByAssignedTo(@PathVariable String assignedTo) {
        return new ResponseEntity<>(this.INQUIRY_SERVICE_IMPL.findByAssignedTo(assignedTo), HttpStatus.OK);
    }

    // ================ FIND BY INSTITUTE AND STATUS ===================== //
    @GetMapping("/institute/{instituteIdentifier}/status/{status}")
    public ResponseEntity<?> findByInstituteIdentifierAndStatus(@PathVariable String instituteIdentifier, @PathVariable InquiryStatus status) {
        return new ResponseEntity<>(this.INQUIRY_SERVICE_IMPL.findByInstituteIdentifierAndStatus(instituteIdentifier, status), HttpStatus.OK);
    }
}
