package com.classes.Backend.Controller.institute;

import com.classes.Backend.Domain.activity.ActivityActionType;
import com.classes.Backend.Domain.activity.ActivityEntityType;
import com.classes.Backend.Domain.institute.Branch;
import com.classes.Backend.Service.activity.ActivityLogActorResolver;
import com.classes.Backend.Service.activity.ActivityLogChangeExtractor;
import com.classes.Backend.Service.activity.ActivityLogService;
import com.classes.Backend.Service.activity.ResolvedActor;
import com.classes.Backend.Service.institute.BranchServiceImpl;
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
@RequestMapping("/api/branches")
public class BranchController {

    private final BranchServiceImpl BRANCH_SERVICE_IMPL;
    private final ActivityLogService ACTIVITY_LOG_SERVICE;
    private final ActivityLogActorResolver ACTOR_RESOLVER;

    // ================ CREATE BRANCH ===================== //
    @PostMapping
    public ResponseEntity<?> saveBranch(@RequestBody Branch branch, HttpServletRequest request) {
        Branch saved = this.BRANCH_SERVICE_IMPL.save(branch);

        ResolvedActor actor = ACTOR_RESOLVER.resolve(request);
        if (actor.isAuthenticated()) {
            ACTIVITY_LOG_SERVICE.log(ActivityLogRequest.builder()
                    .actorType(actor.getType())
                    .actorIdentifier(actor.getIdentifier())
                    .actorName(actor.getName())
                    .actionType(ActivityActionType.BRANCH_CREATED)
                    .entityType(ActivityEntityType.BRANCH)
                    .entityIdentifier(saved.getIdentifier())
                    .entityName(saved.getName())
                    .instituteIdentifier(saved.getInstituteIdentifier())
                    .description("Created branch" + (saved.getName() != null ? " " + saved.getName() : ""))
                    .source("CONSOLE")
                    .build());
        }

        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }

    // ================ CREATE ALL BRANCHES ===================== //
    @PostMapping("/bulk")
    public ResponseEntity<?> saveAllBranches(@RequestBody List<Branch> branches) {
        return new ResponseEntity<>(this.BRANCH_SERVICE_IMPL.saveAll(branches), HttpStatus.CREATED);
    }

    // ================ GET BRANCH BY ID ===================== //
    @GetMapping("/{identifier}")
    public ResponseEntity<?> getBranchById(@PathVariable String identifier) {
        return new ResponseEntity<>(this.BRANCH_SERVICE_IMPL.findById(identifier), HttpStatus.OK);
    }

    // ================ GET ALL BRANCHES ===================== //
    @GetMapping
    public ResponseEntity<?> getAllBranches() {
        List<Branch> allBranches = this.BRANCH_SERVICE_IMPL.findAll();
        return new ResponseEntity<>(allBranches, HttpStatus.OK);
    }

    // ================ DELETE BRANCH BY ID ===================== //
    @DeleteMapping("/{identifier}")
    public ResponseEntity<?> deleteBranchById(@PathVariable String identifier, HttpServletRequest request) {
        Branch existing = this.BRANCH_SERVICE_IMPL.findById(identifier).orElse(null);

        this.BRANCH_SERVICE_IMPL.deleteById(identifier);

        ResolvedActor actor = ACTOR_RESOLVER.resolve(request);
        if (actor.isAuthenticated() && existing != null) {
            ACTIVITY_LOG_SERVICE.log(ActivityLogRequest.builder()
                    .actorType(actor.getType())
                    .actorIdentifier(actor.getIdentifier())
                    .actorName(actor.getName())
                    .actionType(ActivityActionType.BRANCH_DELETED)
                    .entityType(ActivityEntityType.BRANCH)
                    .entityIdentifier(identifier)
                    .entityName(existing.getName())
                    .instituteIdentifier(existing.getInstituteIdentifier())
                    .description("Deleted branch" + (existing.getName() != null ? " " + existing.getName() : ""))
                    .source("CONSOLE")
                    .build());
        }

        return new ResponseEntity<>("Branch deleted successfully", HttpStatus.OK);
    }

    // ================ UPDATE BRANCH BY ID ===================== //
    @PutMapping("/{identifier}")
    public ResponseEntity<?> updateBranchById(@PathVariable String identifier, @RequestBody Branch branch, HttpServletRequest request) {
        Branch existing = this.BRANCH_SERVICE_IMPL.findById(identifier).orElse(null);
        if (existing == null) {
            return new ResponseEntity<>("Branch not found", HttpStatus.NOT_FOUND);
        }
        branch.setIdentifier(identifier);
        Branch updated = this.BRANCH_SERVICE_IMPL.save(branch);

        ResolvedActor actor = ACTOR_RESOLVER.resolve(request);
        if (actor.isAuthenticated()) {
            ACTIVITY_LOG_SERVICE.log(ActivityLogRequest.builder()
                    .actorType(actor.getType())
                    .actorIdentifier(actor.getIdentifier())
                    .actorName(actor.getName())
                    .actionType(ActivityActionType.BRANCH_UPDATED)
                    .entityType(ActivityEntityType.BRANCH)
                    .entityIdentifier(updated.getIdentifier())
                    .entityName(updated.getName())
                    .instituteIdentifier(updated.getInstituteIdentifier())
                    .description("Updated branch" + (updated.getName() != null ? " " + updated.getName() : ""))
                    .metadata(Map.of("changedFields", ActivityLogChangeExtractor.extractChangedFields(existing, updated)))
                    .source("CONSOLE")
                    .build());
        }

        return new ResponseEntity<>(updated, HttpStatus.OK);
    }

    // ================ FIND BY INSTITUTE IDENTIFIER ===================== //
    @GetMapping("/institute/{instituteIdentifier}")
    public ResponseEntity<?> findByInstituteIdentifier(@PathVariable String instituteIdentifier) {
        return new ResponseEntity<>(this.BRANCH_SERVICE_IMPL.findByInstituteIdentifier(instituteIdentifier), HttpStatus.OK);
    }

    // ================ FIND BY CITY IDENTIFIER ===================== //
    @GetMapping("/city/{cityIdentifier}")
    public ResponseEntity<?> findByCityIdentifier(@PathVariable String cityIdentifier) {
        return new ResponseEntity<>(this.BRANCH_SERVICE_IMPL.findByCityIdentifier(cityIdentifier), HttpStatus.OK);
    }

    // ================ FIND MAIN BRANCH BY INSTITUTE ===================== //
    @GetMapping("/institute/{instituteIdentifier}/main")
    public ResponseEntity<?> findByInstituteIdentifierAndIsMainBranchTrue(@PathVariable String instituteIdentifier) {
        return new ResponseEntity<>(this.BRANCH_SERVICE_IMPL.findByInstituteIdentifierAndIsMainBranchTrue(instituteIdentifier), HttpStatus.OK);
    }

    // ================ FIND ONLINE ONLY BRANCHES ===================== //
    @GetMapping("/online-only")
    public ResponseEntity<?> findByIsOnlineOnlyTrue() {
        return new ResponseEntity<>(this.BRANCH_SERVICE_IMPL.findByIsOnlineOnlyTrue(), HttpStatus.OK);
    }

}
