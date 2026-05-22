package com.classes.Backend.Controller.leads;

import com.classes.Backend.Domain.enums.LeadStatus;
import com.classes.Backend.Domain.leads.Lead;
import com.classes.Backend.Domain.users.User;
import com.classes.Backend.Service.auth.JwtService;
import com.classes.Backend.Service.leads.LeadServiceImpl;
import com.classes.Backend.Service.users.UserService;
import com.classes.Backend.dto.leads.LeadCreateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/leads")
public class LeadController {

    private final LeadServiceImpl LEAD_SERVICE_IMPL;
    private final JwtService JWT_SERVICE;
    private final UserService USER_SERVICE;

    @PostMapping
    public ResponseEntity<?> createLead(@RequestBody LeadCreateRequest request,
                                         @RequestHeader(value = "Authorization", required = false) String authHeader) {
        Lead lead = new Lead();
        lead.setPhone(request.getPhone());
        lead.setFullName(request.getFullName());
        lead.setCityIdentifier(request.getCityIdentifier());
        lead.setExamTypeIdentifier(request.getExamTypeIdentifier());
        lead.setSearchedQuery(request.getSearchedQuery());
        lead.setVisitedInstituteIdentifier(request.getVisitedInstituteIdentifier());
        lead.setVisitedInstituteName(request.getVisitedInstituteName());
        lead.setSource(request.getSource());

        // If user is authenticated, attach user identifier
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            try {
                String token = authHeader.substring(7);
                String username = JWT_SERVICE.extractUsername(token);
                User user = USER_SERVICE.findByEmail(username)
                        .orElseGet(() -> USER_SERVICE.findByPhone(username).orElse(null));
                if (user != null) {
                    lead.setUserIdentifier(user.getIdentifier());
                    lead.setPhone(user.getPhone());
                    lead.setFullName(user.getFullName());
                }
            } catch (Exception e) {
                // Ignore auth errors for anonymous tracking
            }
        }

        Lead saved = LEAD_SERVICE_IMPL.save(lead);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @GetMapping
    public ResponseEntity<?> getAllLeads(
            @RequestParam(required = false) LeadStatus status,
            @RequestParam(required = false) String cityIdentifier,
            @RequestParam(required = false) String examTypeIdentifier,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) LocalDateTime dateFrom,
            @RequestParam(required = false) LocalDateTime dateTo) {

        List<Lead> leads;
        if (status != null) {
            leads = LEAD_SERVICE_IMPL.findByStatus(status);
        } else if (cityIdentifier != null) {
            leads = LEAD_SERVICE_IMPL.findByCityIdentifier(cityIdentifier);
        } else if (examTypeIdentifier != null) {
            leads = LEAD_SERVICE_IMPL.findByExamTypeIdentifier(examTypeIdentifier);
        } else if (search != null && !search.isBlank()) {
            leads = LEAD_SERVICE_IMPL.searchByPhoneOrName(search);
        } else if (dateFrom != null && dateTo != null) {
            leads = LEAD_SERVICE_IMPL.findByCreatedAtBetween(dateFrom, dateTo);
        } else {
            leads = LEAD_SERVICE_IMPL.findAll();
        }
        return ResponseEntity.ok(leads);
    }

    @GetMapping("/my")
    public ResponseEntity<?> getMyLeads(@RequestHeader("Authorization") String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Unauthorized"));
        }
        String token = authHeader.substring(7);
        String username = JWT_SERVICE.extractUsername(token);
        User user = USER_SERVICE.findByEmail(username)
                .orElseGet(() -> USER_SERVICE.findByPhone(username).orElse(null));
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "User not found"));
        }
        List<Lead> leads = LEAD_SERVICE_IMPL.findByUserIdentifier(user.getIdentifier());
        return ResponseEntity.ok(leads);
    }

    @GetMapping("/institute/{instituteIdentifier}")
    public ResponseEntity<?> getLeadsByInstitute(@PathVariable String instituteIdentifier) {
        List<Lead> leads = LEAD_SERVICE_IMPL.findByVisitedInstituteIdentifier(instituteIdentifier);
        return ResponseEntity.ok(leads);
    }

    @PatchMapping("/{identifier}/status")
    public ResponseEntity<?> updateLeadStatus(@PathVariable String identifier,
                                               @RequestBody Map<String, String> request) {
        try {
            LeadStatus status = LeadStatus.valueOf(request.get("status"));
            Lead updated = LEAD_SERVICE_IMPL.updateStatus(identifier, status);
            return ResponseEntity.ok(updated);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", "Invalid status value"));
        }
    }
}
