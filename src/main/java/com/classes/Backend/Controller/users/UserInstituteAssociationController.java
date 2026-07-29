package com.classes.Backend.Controller.users;

import com.classes.Backend.Domain.activity.ActivityActionType;
import com.classes.Backend.Domain.activity.ActivityEntityType;
import com.classes.Backend.Domain.enums.InstituteStaffRole;
import com.classes.Backend.Domain.users.UserInstituteAssociation;
import com.classes.Backend.Service.activity.ActivityLogActorResolver;
import com.classes.Backend.Service.activity.ActivityLogService;
import com.classes.Backend.Service.activity.ResolvedActor;
import com.classes.Backend.Service.users.UserInstituteAssociationServiceImpl;
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
@RequestMapping("/api/user-institute-associations")
public class UserInstituteAssociationController {

    private final UserInstituteAssociationServiceImpl USER_INSTITUTE_ASSOCIATION_SERVICE_IMPL;
    private final ActivityLogService ACTIVITY_LOG_SERVICE;
    private final ActivityLogActorResolver ACTOR_RESOLVER;

    // ================ CREATE USER INSTITUTE ASSOCIATION ===================== //
    @PostMapping
    public ResponseEntity<?> saveUserInstituteAssociation(@RequestBody UserInstituteAssociation userInstituteAssociation, HttpServletRequest request) {
        UserInstituteAssociation saved = this.USER_INSTITUTE_ASSOCIATION_SERVICE_IMPL.save(userInstituteAssociation);

        ResolvedActor actor = ACTOR_RESOLVER.resolve(request);
        if (actor.isAuthenticated()) {
            ACTIVITY_LOG_SERVICE.log(ActivityLogRequest.builder()
                    .actorType(actor.getType())
                    .actorIdentifier(actor.getIdentifier())
                    .actorName(actor.getName())
                    .actionType(ActivityActionType.STAFF_ADDED)
                    .entityType(ActivityEntityType.USER_INSTITUTE_ASSOCIATION)
                    .entityIdentifier(saved.getIdentifier())
                    .entityName(saved.getUserIdentifier() + " as " + saved.getRole())
                    .instituteIdentifier(saved.getInstituteIdentifier())
                    .description("Added staff member" + (saved.getRole() != null ? " as " + saved.getRole().name() : ""))
                    .metadata(Map.of(
                            "role", saved.getRole() != null ? saved.getRole().name() : null,
                            "isActive", saved.getIsActive()
                    ))
                    .source("CONSOLE")
                    .build());
        }

        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }

    // ================ CREATE ALL USER INSTITUTE ASSOCIATIONS ===================== //
    @PostMapping("/bulk")
    public ResponseEntity<?> saveAllUserInstituteAssociations(@RequestBody List<UserInstituteAssociation> userInstituteAssociations) {
        return new ResponseEntity<>(this.USER_INSTITUTE_ASSOCIATION_SERVICE_IMPL.saveAll(userInstituteAssociations), HttpStatus.CREATED);
    }

    // ================ GET USER INSTITUTE ASSOCIATION BY ID ===================== //
    @GetMapping("/{identifier}")
    public ResponseEntity<?> getUserInstituteAssociationById(@PathVariable String identifier) {
        return new ResponseEntity<>(this.USER_INSTITUTE_ASSOCIATION_SERVICE_IMPL.findById(identifier), HttpStatus.OK);
    }

    // ================ GET ALL USER INSTITUTE ASSOCIATIONS ===================== //
    @GetMapping
    public ResponseEntity<?> getAllUserInstituteAssociations() {
        List<UserInstituteAssociation> allAssociations = this.USER_INSTITUTE_ASSOCIATION_SERVICE_IMPL.findAll();
        return new ResponseEntity<>(allAssociations, HttpStatus.OK);
    }

    // ================ DELETE USER INSTITUTE ASSOCIATION BY ID ===================== //
    @DeleteMapping("/{identifier}")
    public ResponseEntity<?> deleteUserInstituteAssociationById(@PathVariable String identifier, HttpServletRequest request) {
        UserInstituteAssociation existing = this.USER_INSTITUTE_ASSOCIATION_SERVICE_IMPL.findById(identifier).orElse(null);

        this.USER_INSTITUTE_ASSOCIATION_SERVICE_IMPL.deleteById(identifier);

        ResolvedActor actor = ACTOR_RESOLVER.resolve(request);
        if (actor.isAuthenticated() && existing != null) {
            ACTIVITY_LOG_SERVICE.log(ActivityLogRequest.builder()
                    .actorType(actor.getType())
                    .actorIdentifier(actor.getIdentifier())
                    .actorName(actor.getName())
                    .actionType(ActivityActionType.STAFF_REMOVED)
                    .entityType(ActivityEntityType.USER_INSTITUTE_ASSOCIATION)
                    .entityIdentifier(identifier)
                    .entityName(existing.getUserIdentifier() + " as " + existing.getRole())
                    .instituteIdentifier(existing.getInstituteIdentifier())
                    .description("Removed staff member" + (existing.getRole() != null ? " as " + existing.getRole().name() : ""))
                    .metadata(Map.of(
                            "role", existing.getRole() != null ? existing.getRole().name() : null,
                            "isActive", existing.getIsActive()
                    ))
                    .source("CONSOLE")
                    .build());
        }

        return new ResponseEntity<>("UserInstituteAssociation deleted successfully", HttpStatus.OK);
    }

    // ================ UPDATE USER INSTITUTE ASSOCIATION BY ID ===================== //
    @PutMapping("/{identifier}")
    public ResponseEntity<?> updateUserInstituteAssociationById(@PathVariable String identifier, @RequestBody UserInstituteAssociation userInstituteAssociation, HttpServletRequest request) {
        UserInstituteAssociation existing = this.USER_INSTITUTE_ASSOCIATION_SERVICE_IMPL.findById(identifier).orElse(null);
        if (existing == null) {
            return new ResponseEntity<>("UserInstituteAssociation not found", HttpStatus.NOT_FOUND);
        }
        userInstituteAssociation.setIdentifier(identifier);
        UserInstituteAssociation updated = this.USER_INSTITUTE_ASSOCIATION_SERVICE_IMPL.save(userInstituteAssociation);

        ResolvedActor actor = ACTOR_RESOLVER.resolve(request);
        if (actor.isAuthenticated()) {
            ActivityActionType action = existing.getRole() != updated.getRole()
                    ? ActivityActionType.STAFF_ROLE_CHANGED
                    : ActivityActionType.STAFF_ROLE_CHANGED;
            ACTIVITY_LOG_SERVICE.log(ActivityLogRequest.builder()
                    .actorType(actor.getType())
                    .actorIdentifier(actor.getIdentifier())
                    .actorName(actor.getName())
                    .actionType(action)
                    .entityType(ActivityEntityType.USER_INSTITUTE_ASSOCIATION)
                    .entityIdentifier(updated.getIdentifier())
                    .entityName(updated.getUserIdentifier() + " as " + updated.getRole())
                    .instituteIdentifier(updated.getInstituteIdentifier())
                    .description("Changed staff role" + (updated.getRole() != null ? " to " + updated.getRole().name() : ""))
                    .metadata(Map.of(
                            "oldRole", existing.getRole() != null ? existing.getRole().name() : null,
                            "newRole", updated.getRole() != null ? updated.getRole().name() : null,
                            "isActive", updated.getIsActive()
                    ))
                    .source("CONSOLE")
                    .build());
        }

        return new ResponseEntity<>(updated, HttpStatus.OK);
    }

    // ================ FIND BY USER IDENTIFIER ===================== //
    @GetMapping("/user/{userIdentifier}")
    public ResponseEntity<?> findByUserIdentifier(@PathVariable String userIdentifier) {
        return new ResponseEntity<>(this.USER_INSTITUTE_ASSOCIATION_SERVICE_IMPL.findByUserIdentifier(userIdentifier), HttpStatus.OK);
    }

    // ================ FIND BY INSTITUTE IDENTIFIER ===================== //
    @GetMapping("/institute/{instituteIdentifier}")
    public ResponseEntity<?> findByInstituteIdentifier(@PathVariable String instituteIdentifier) {
        return new ResponseEntity<>(this.USER_INSTITUTE_ASSOCIATION_SERVICE_IMPL.findByInstituteIdentifier(instituteIdentifier), HttpStatus.OK);
    }

    // ================ FIND BY USER AND INSTITUTE ===================== //
    @GetMapping("/user/{userIdentifier}/institute/{instituteIdentifier}")
    public ResponseEntity<?> findByUserIdentifierAndInstituteIdentifier(@PathVariable String userIdentifier, @PathVariable String instituteIdentifier) {
        return new ResponseEntity<>(this.USER_INSTITUTE_ASSOCIATION_SERVICE_IMPL.findByUserIdentifierAndInstituteIdentifier(userIdentifier, instituteIdentifier), HttpStatus.OK);
    }

    // ================ FIND BY ROLE ===================== //
    @GetMapping("/role/{role}")
    public ResponseEntity<?> findByRole(@PathVariable InstituteStaffRole role) {
        return new ResponseEntity<>(this.USER_INSTITUTE_ASSOCIATION_SERVICE_IMPL.findByRole(role), HttpStatus.OK);
    }

    // ================ FIND ACTIVE ASSOCIATIONS ===================== //
    @GetMapping("/active")
    public ResponseEntity<?> findByIsActiveTrue() {
        return new ResponseEntity<>(this.USER_INSTITUTE_ASSOCIATION_SERVICE_IMPL.findByIsActiveTrue(), HttpStatus.OK);
    }
}
