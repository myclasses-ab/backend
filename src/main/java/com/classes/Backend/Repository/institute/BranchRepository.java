package com.classes.Backend.Repository.institute;

import com.classes.Backend.Domain.institute.Branch;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BranchRepository extends JpaRepository<Branch, String> {
    List<Branch> findByInstituteIdentifier(String instituteIdentifier);
    List<Branch> findByCityIdentifier(String cityIdentifier);
    Optional<Branch> findByInstituteIdentifierAndIsMainBranchTrue(String instituteIdentifier);
    List<Branch> findByIsOnlineOnlyTrue();
}
