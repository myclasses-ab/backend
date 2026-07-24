package com.classes.Backend.Service.institute;

import com.classes.Backend.Domain.institute.Branch;
import com.classes.Backend.Repository.institute.BranchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class BranchServiceImpl implements BranchService {
    private final BranchRepository BRANCH_REPOSITORY;

    // ================ SAVE BRANCH ===================== //
    @Override
    public Branch save(Branch branch) {
        normalizeServiceCities(branch);
        return this.BRANCH_REPOSITORY.save(branch);
    }

    private void normalizeServiceCities(Branch branch) {
        List<String> cities = branch.getServiceCities();
        if (cities == null) {
            cities = new ArrayList<>();
        }

        // Backfill from legacy cityName if the list is empty
        if (cities.isEmpty() && branch.getCityName() != null && !branch.getCityName().trim().isEmpty()) {
            cities.add(branch.getCityName().trim());
        }

        List<String> normalized = cities.stream()
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .distinct()
                .collect(Collectors.toList());

        if (CollectionUtils.isEmpty(normalized)) {
            branch.setServiceCities(new ArrayList<>());
            return;
        }

        branch.setServiceCities(normalized);
        branch.setCityName(normalized.get(0));
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
