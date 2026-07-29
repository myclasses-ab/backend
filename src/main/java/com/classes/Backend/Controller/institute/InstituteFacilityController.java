package com.classes.Backend.Controller.institute;

import com.classes.Backend.Domain.activity.ActivityActionType;
import com.classes.Backend.Domain.activity.ActivityEntityType;
import com.classes.Backend.Domain.institute.InstituteFacility;
import com.classes.Backend.Service.activity.ActivityLogActorResolver;
import com.classes.Backend.Service.activity.ActivityLogChangeExtractor;
import com.classes.Backend.Service.activity.ActivityLogService;
import com.classes.Backend.Service.activity.ResolvedActor;
import com.classes.Backend.Service.institute.InstituteFacilityServiceImpl;
import com.classes.Backend.dto.activity.ActivityLogRequest;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/institute-facilities")
public class InstituteFacilityController {

    private final InstituteFacilityServiceImpl INSTITUTE_FACILITY_SERVICE_IMPL;
    private final ActivityLogService ACTIVITY_LOG_SERVICE;
    private final ActivityLogActorResolver ACTOR_RESOLVER;

    // ================ CREATE INSTITUTE FACILITY ===================== //
    @PostMapping
    public ResponseEntity<?> saveInstituteFacility(@RequestBody InstituteFacility instituteFacility, HttpServletRequest request) {
        InstituteFacility saved = this.INSTITUTE_FACILITY_SERVICE_IMPL.save(instituteFacility);

        ResolvedActor actor = ACTOR_RESOLVER.resolve(request);
        if (actor.isAuthenticated()) {
            ACTIVITY_LOG_SERVICE.log(ActivityLogRequest.builder()
                    .actorType(actor.getType())
                    .actorIdentifier(actor.getIdentifier())
                    .actorName(actor.getName())
                    .actionType(ActivityActionType.FACILITY_CREATED)
                    .entityType(ActivityEntityType.FACILITY)
                    .entityIdentifier(saved.getIdentifier())
                    .entityName("Facilities")
                    .instituteIdentifier(saved.getInstituteIdentifier())
                    .description("Created institute facilities")
                    .source("CONSOLE")
                    .build());
        }

        return new ResponseEntity<>(saved, HttpStatus.CREATED);
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
    public ResponseEntity<?> deleteInstituteFacilityById(@PathVariable String identifier, HttpServletRequest request) {
        InstituteFacility existing = this.INSTITUTE_FACILITY_SERVICE_IMPL.findById(identifier).orElse(null);

        this.INSTITUTE_FACILITY_SERVICE_IMPL.deleteById(identifier);

        ResolvedActor actor = ACTOR_RESOLVER.resolve(request);
        if (actor.isAuthenticated() && existing != null) {
            ACTIVITY_LOG_SERVICE.log(ActivityLogRequest.builder()
                    .actorType(actor.getType())
                    .actorIdentifier(actor.getIdentifier())
                    .actorName(actor.getName())
                    .actionType(ActivityActionType.FACILITY_UPDATED)
                    .entityType(ActivityEntityType.FACILITY)
                    .entityIdentifier(identifier)
                    .entityName("Facilities")
                    .instituteIdentifier(existing.getInstituteIdentifier())
                    .description("Deleted institute facilities")
                    .source("CONSOLE")
                    .build());
        }

        return new ResponseEntity<>("InstituteFacility deleted successfully", HttpStatus.OK);
    }

    // ================ UPDATE INSTITUTEFACILITY BY ID ===================== //
    @PutMapping("/{identifier}")
    public ResponseEntity<?> updateInstituteFacilityById(@PathVariable String identifier, @RequestBody InstituteFacility instituteFacility, HttpServletRequest request) {
        InstituteFacility existing = this.INSTITUTE_FACILITY_SERVICE_IMPL.findById(identifier).orElse(null);
        if (existing == null) {
            return new ResponseEntity<>("InstituteFacility not found", HttpStatus.NOT_FOUND);
        }
        instituteFacility.setIdentifier(identifier);
        InstituteFacility updated = this.INSTITUTE_FACILITY_SERVICE_IMPL.save(instituteFacility);

        ResolvedActor actor = ACTOR_RESOLVER.resolve(request);
        if (actor.isAuthenticated()) {
            ACTIVITY_LOG_SERVICE.log(ActivityLogRequest.builder()
                    .actorType(actor.getType())
                    .actorIdentifier(actor.getIdentifier())
                    .actorName(actor.getName())
                    .actionType(ActivityActionType.FACILITY_UPDATED)
                    .entityType(ActivityEntityType.FACILITY)
                    .entityIdentifier(updated.getIdentifier())
                    .entityName("Facilities")
                    .instituteIdentifier(updated.getInstituteIdentifier())
                    .description("Updated institute facilities")
                    .metadata(Map.of("changedFields", ActivityLogChangeExtractor.extractChangedFields(existing, updated)))
                    .source("CONSOLE")
                    .build());
        }

        return new ResponseEntity<>(updated, HttpStatus.OK);
    }

    // ================ FIND BY INSTITUTE IDENTIFIER ===================== //
    @GetMapping("/institute/{instituteIdentifier}")
    public ResponseEntity<?> findByInstituteIdentifier(@PathVariable String instituteIdentifier) {
        return new ResponseEntity<>(this.INSTITUTE_FACILITY_SERVICE_IMPL.findByInstituteIdentifier(instituteIdentifier), HttpStatus.OK);
    }
}
