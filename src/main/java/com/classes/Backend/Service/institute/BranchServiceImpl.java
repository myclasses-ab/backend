package com.classes.Backend.Service.institute;

import com.classes.Backend.Domain.institute.Branch;
import com.classes.Backend.Repository.institute.BranchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class BranchServiceImpl implements BranchService {
    private final BranchRepository BRANCH_REPOSITORY;

    // ================ SAVE BRANCH ===================== //
    @Override
    public Branch save(Branch branch) {
        return this.BRANCH_REPOSITORY.save(branch);
    }

    // ================ SAVE ALL BRANCHES ===================== //
    @Override
    public List<Branch> saveAll(List<Branch> branches) {
        return this.BRANCH_REPOSITORY.saveAll(branches);
    }

    // ================ FIND BY ID ===================== //
    @Override
    public Optional<Branch> findById(String identifier) {
        return this.BRANCH_REPOSITORY.findById(identifier);
    }

    // ================ FIND ALL ===================== //
    @Override
    public List<Branch> findAll() {
        return this.BRANCH_REPOSITORY.findAll();
    }

    // ================ DELETE BY ID ===================== //
    @Override
    public void deleteById(String identifier) {
        if (!this.BRANCH_REPOSITORY.existsById(identifier)) {
            throw new RuntimeException("Branch with identifier '" + identifier + "' not found");
        }
        this.BRANCH_REPOSITORY.deleteById(identifier);
    }

    // ================ EXISTS BY ID ===================== //
    @Override
    public boolean existsById(String identifier) {
        return this.BRANCH_REPOSITORY.existsById(identifier);
    }

    // ================ FIND BY INSTITUTE IDENTIFIER ===================== //
    @Override
    public List<Branch> findByInstituteIdentifier(String instituteIdentifier) {
        return this.BRANCH_REPOSITORY.findByInstituteIdentifier(instituteIdentifier);
    }

    // ================ FIND BY CITY IDENTIFIER ===================== //
    @Override
    public List<Branch> findByCityIdentifier(String cityIdentifier) {
        return this.BRANCH_REPOSITORY.findByCityIdentifier(cityIdentifier);
    }

    // ================ FIND BY INSTITUTE IDENTIFIER AND IS MAIN BRANCH TRUE ===================== //
    @Override
    public Optional<Branch> findByInstituteIdentifierAndIsMainBranchTrue(String instituteIdentifier) {
        return this.BRANCH_REPOSITORY.findByInstituteIdentifierAndIsMainBranchTrue(instituteIdentifier);
    }

    // ================ FIND BY IS ONLINE ONLY TRUE ===================== //
    @Override
    public List<Branch> findByIsOnlineOnlyTrue() {
        return this.BRANCH_REPOSITORY.findByIsOnlineOnlyTrue();
    }
}
