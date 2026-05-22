package com.classes.Backend.Controller.leads;

import com.classes.Backend.Domain.leads.LeadDistribution;
import com.classes.Backend.Domain.users.User;
import com.classes.Backend.Service.leads.LeadDistributionServiceImpl;
import com.classes.Backend.Service.users.UserServiceImpl;
import com.classes.Backend.dto.leads.LeadDistributionRequest;
import com.classes.Backend.dto.leads.LeadDistributionUpdateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/lead-distributions")
public class LeadDistributionController {

    private final LeadDistributionServiceImpl LEAD_DISTRIBUTION_SERVICE_IMPL;
    private final UserServiceImpl USER_SERVICE_IMPL;

    @PostMapping
    public ResponseEntity<?> createDistributions(@RequestBody LeadDistributionRequest request) {
        if (request.getUserIdentifiers() == null || request.getUserIdentifiers().isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("error", "No users selected"));
        }
        if (request.getInstituteIdentifier() == null || request.getInstituteIdentifier().isBlank()) {
            return ResponseEntity.badRequest().body(Map.of("error", "Institute identifier required"));
        }

        List<LeadDistribution> created = new ArrayList<>();
        for (String userId : request.getUserIdentifiers()) {
            User user = USER_SERVICE_IMPL.findById(userId).orElse(null);
            if (user == null) continue;

            LeadDistribution distribution = new LeadDistribution();
            distribution.setUserIdentifier(userId);
            distribution.setUserName(user.getFullName());
            distribution.setUserPhone(user.getPhone());
            distribution.setInstituteIdentifier(request.getInstituteIdentifier());
            distribution.setInstituteName(user.getVisitedInstituteNames() != null && !user.getVisitedInstituteNames().isEmpty()
                ? user.getVisitedInstituteNames().get(user.getVisitedInstituteNames().size() - 1) : null);
            distribution.setNotes(request.getNotes());

            created.add(LEAD_DISTRIBUTION_SERVICE_IMPL.save(distribution));
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping
    public ResponseEntity<?> getAllDistributions() {
        List<LeadDistribution> distributions = LEAD_DISTRIBUTION_SERVICE_IMPL.findAll();
        return ResponseEntity.ok(distributions);
    }

    @GetMapping("/institute/{instituteIdentifier}")
    public ResponseEntity<?> getDistributionsByInstitute(@PathVariable String instituteIdentifier) {
        List<LeadDistribution> distributions = LEAD_DISTRIBUTION_SERVICE_IMPL.findByInstituteIdentifier(instituteIdentifier);
        return ResponseEntity.ok(distributions);
    }

    @GetMapping("/user/{userIdentifier}")
    public ResponseEntity<?> getDistributionsByUser(@PathVariable String userIdentifier) {
        List<LeadDistribution> distributions = LEAD_DISTRIBUTION_SERVICE_IMPL.findByUserIdentifier(userIdentifier);
        return ResponseEntity.ok(distributions);
    }

    @PatchMapping("/{identifier}")
    public ResponseEntity<?> updateDistribution(@PathVariable String identifier,
                                                 @RequestBody LeadDistributionUpdateRequest request) {
        LeadDistribution updated = LEAD_DISTRIBUTION_SERVICE_IMPL.findById(identifier)
                .orElseThrow(() -> new RuntimeException("LeadDistribution not found with identifier: " + identifier));

        if (request.getStatus() != null) {
            updated.setStatus(request.getStatus());
        }
        if (request.getNotes() != null) {
            updated.setNotes(request.getNotes());
        }
        if (request.getInstituteNotes() != null) {
            updated.setInstituteNotes(request.getInstituteNotes());
        }

        updated = LEAD_DISTRIBUTION_SERVICE_IMPL.save(updated);
        return ResponseEntity.ok(updated);
    }
}
