package com.classes.Backend.Controller.users;

import com.classes.Backend.Domain.enums.InstituteStaffRole;
import com.classes.Backend.Domain.users.UserInstituteAssociation;
import com.classes.Backend.Service.users.UserInstituteAssociationServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user-institute-associations")
public class UserInstituteAssociationController {

    private final UserInstituteAssociationServiceImpl USER_INSTITUTE_ASSOCIATION_SERVICE_IMPL;

    // ================ CREATE USER INSTITUTE ASSOCIATION ===================== //
    @PostMapping
    public ResponseEntity<?> saveUserInstituteAssociation(@RequestBody UserInstituteAssociation userInstituteAssociation) {
        return new ResponseEntity<>(this.USER_INSTITUTE_ASSOCIATION_SERVICE_IMPL.save(userInstituteAssociation), HttpStatus.CREATED);
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
    public ResponseEntity<?> deleteUserInstituteAssociationById(@PathVariable String identifier) {
        this.USER_INSTITUTE_ASSOCIATION_SERVICE_IMPL.deleteById(identifier);
        return new ResponseEntity<>("UserInstituteAssociation deleted successfully", HttpStatus.OK);
    }

    // ================ UPDATE USER INSTITUTE ASSOCIATION BY ID ===================== //
    @PutMapping("/{identifier}")
    public ResponseEntity<?> updateUserInstituteAssociationById(@PathVariable String identifier, @RequestBody UserInstituteAssociation userInstituteAssociation) {
        if (!this.USER_INSTITUTE_ASSOCIATION_SERVICE_IMPL.existsById(identifier)) {
            return new ResponseEntity<>("UserInstituteAssociation not found", HttpStatus.NOT_FOUND);
        }
        userInstituteAssociation.setIdentifier(identifier);
        return new ResponseEntity<>(this.USER_INSTITUTE_ASSOCIATION_SERVICE_IMPL.save(userInstituteAssociation), HttpStatus.OK);
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
