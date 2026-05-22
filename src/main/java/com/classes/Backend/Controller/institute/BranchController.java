package com.classes.Backend.Controller.institute;

import com.classes.Backend.Domain.institute.Branch;
import com.classes.Backend.Service.institute.BranchServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/branches")
public class BranchController {

    private final BranchServiceImpl BRANCH_SERVICE_IMPL;

    // ================ CREATE BRANCH ===================== //
    @PostMapping
    public ResponseEntity<?> saveBranch(@RequestBody Branch branch) {
        return new ResponseEntity<>(this.BRANCH_SERVICE_IMPL.save(branch), HttpStatus.CREATED);
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
    public ResponseEntity<?> deleteBranchById(@PathVariable String identifier) {
        this.BRANCH_SERVICE_IMPL.deleteById(identifier);
        return new ResponseEntity<>("Branch deleted successfully", HttpStatus.OK);
    }

    // ================ UPDATE BRANCH BY ID ===================== //
    @PutMapping("/{identifier}")
    public ResponseEntity<?> updateBranchById(@PathVariable String identifier, @RequestBody Branch branch) {
        if (!this.BRANCH_SERVICE_IMPL.existsById(identifier)) {
            return new ResponseEntity<>("Branch not found", HttpStatus.NOT_FOUND);
        }
        branch.setIdentifier(identifier);
        return new ResponseEntity<>(this.BRANCH_SERVICE_IMPL.save(branch), HttpStatus.OK);
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

    // ================ FIND ACTIVE BRANCHES ===================== //
    @GetMapping("/active")
    public ResponseEntity<?> findByIsActiveTrue() {
        return new ResponseEntity<>(this.BRANCH_SERVICE_IMPL.findByIsActiveTrue(), HttpStatus.OK);
    }
}
