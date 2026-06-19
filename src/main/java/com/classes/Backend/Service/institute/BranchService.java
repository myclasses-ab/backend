package com.classes.Backend.Service.institute;

import com.classes.Backend.Domain.institute.Branch;

import java.util.List;
import java.util.Optional;

public interface BranchService {
    // ================ CRUD OPERATIONS ===================== //
    Branch save(Branch branch);
    List<Branch> saveAll(List<Branch> branches);
    Optional<Branch> findById(String identifier);
    List<Branch> findAll();
    void deleteById(String identifier);
    boolean existsById(String identifier);

    // ================ CUSTOM FINDER METHODS ===================== //
    List<Branch> findByInstituteIdentifier(String instituteIdentifier);
    List<Branch> findByCityIdentifier(String cityIdentifier);
    Optional<Branch> findByInstituteIdentifierAndIsMainBranchTrue(String instituteIdentifier);
    List<Branch> findByIsOnlineOnlyTrue();
}
