package com.classes.Backend.Controller.leads;

import com.classes.Backend.Domain.enums.LeadRequestStatus;
import com.classes.Backend.Domain.leads.LeadRequest;
import com.classes.Backend.dto.credits.CreateLeadRequest;
import com.classes.Backend.dto.credits.UpdateLeadRequestStatus;
import com.classes.Backend.Service.leads.LeadRequestServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/lead-requests")
public class LeadRequestController {

    private final LeadRequestServiceImpl LEAD_REQUEST_SERVICE_IMPL;

    @PostMapping
    public ResponseEntity<?> createLeadRequest(@RequestBody CreateLeadRequest request) {
        try {
            LeadRequest leadRequest = LEAD_REQUEST_SERVICE_IMPL.createRequest(
                    request.getInstituteIdentifier(),
                    request.getExamTypeIdentifier(),
                    request.getQuantity(),
                    request.getNotes()
            );
            return new ResponseEntity<>(leadRequest, HttpStatus.CREATED);
        } catch (IllegalArgumentException | IllegalStateException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping
    public ResponseEntity<?> getAllLeadRequests() {
        return ResponseEntity.ok(LEAD_REQUEST_SERVICE_IMPL.findAll());
    }

    @GetMapping("/institute/{instituteIdentifier}")
    public ResponseEntity<?> getLeadRequestsByInstitute(@PathVariable String instituteIdentifier) {
        return ResponseEntity.ok(LEAD_REQUEST_SERVICE_IMPL.findByInstitute(instituteIdentifier));
    }

    @GetMapping("/pending")
    public ResponseEntity<?> getPendingLeadRequests() {
        return ResponseEntity.ok(LEAD_REQUEST_SERVICE_IMPL.findPending());
    }

    @PutMapping("/{identifier}/status")
    public ResponseEntity<?> updateLeadRequestStatus(@PathVariable String identifier, @RequestBody UpdateLeadRequestStatus request) {
        try {
            LeadRequest updated;
            if (request.getStatus() == LeadRequestStatus.APPROVED) {
                updated = LEAD_REQUEST_SERVICE_IMPL.approveRequest(identifier, request.getAdminNotes());
            } else if (request.getStatus() == LeadRequestStatus.REJECTED) {
                updated = LEAD_REQUEST_SERVICE_IMPL.rejectRequest(identifier, request.getAdminNotes());
            } else {
                return ResponseEntity.badRequest().body(Map.of("error", "Invalid status transition"));
            }
            return ResponseEntity.ok(updated);
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PatchMapping("/{identifier}/fulfill")
    public ResponseEntity<?> fulfillLeadRequest(@PathVariable String identifier) {
        try {
            LeadRequest updated = LEAD_REQUEST_SERVICE_IMPL.fulfillRequest(identifier);
            return ResponseEntity.ok(updated);
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PatchMapping("/{identifier}/cancel")
    public ResponseEntity<?> cancelLeadRequest(@PathVariable String identifier) {
        try {
            LeadRequest updated = LEAD_REQUEST_SERVICE_IMPL.cancelRequest(identifier);
            return ResponseEntity.ok(updated);
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}
