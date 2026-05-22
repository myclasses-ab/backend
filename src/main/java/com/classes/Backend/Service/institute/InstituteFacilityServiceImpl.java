package com.classes.Backend.Service.institute;

import com.classes.Backend.Domain.institute.InstituteFacility;
import com.classes.Backend.Repository.institute.InstituteFacilityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class InstituteFacilityServiceImpl implements InstituteFacilityService {
    private final InstituteFacilityRepository INSTITUTE_FACILITY_REPOSITORY;

    // ================ SAVE INSTITUTE FACILITY ===================== //
    @Override
    public InstituteFacility save(InstituteFacility instituteFacility) {
        return this.INSTITUTE_FACILITY_REPOSITORY.save(instituteFacility);
    }

    // ================ SAVE ALL INSTITUTE FACILITIES ===================== //
    @Override
    public List<InstituteFacility> saveAll(List<InstituteFacility> instituteFacilities) {
        return this.INSTITUTE_FACILITY_REPOSITORY.saveAll(instituteFacilities);
    }

    // ================ FIND BY ID ===================== //
    @Override
    public Optional<InstituteFacility> findById(String identifier) {
        return this.INSTITUTE_FACILITY_REPOSITORY.findById(identifier);
    }

    // ================ FIND ALL ===================== //
    @Override
    public List<InstituteFacility> findAll() {
        return this.INSTITUTE_FACILITY_REPOSITORY.findAll();
    }

    // ================ DELETE BY ID ===================== //
    @Override
    public void deleteById(String identifier) {
        if (!this.INSTITUTE_FACILITY_REPOSITORY.existsById(identifier)) {
            throw new RuntimeException("InstituteFacility with identifier '" + identifier + "' not found");
        }
        this.INSTITUTE_FACILITY_REPOSITORY.deleteById(identifier);
    }

    // ================ EXISTS BY ID ===================== //
    @Override
    public boolean existsById(String identifier) {
        return this.INSTITUTE_FACILITY_REPOSITORY.existsById(identifier);
    }

    // ================ FIND BY INSTITUTE IDENTIFIER ===================== //
    @Override
    public Optional<InstituteFacility> findByInstituteIdentifier(String instituteIdentifier) {
        return this.INSTITUTE_FACILITY_REPOSITORY.findByInstituteIdentifier(instituteIdentifier);
    }
}
