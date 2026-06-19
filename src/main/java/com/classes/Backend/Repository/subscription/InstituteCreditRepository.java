package com.classes.Backend.Repository.subscription;

import com.classes.Backend.Domain.subscription.InstituteCredit;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface InstituteCreditRepository extends JpaRepository<InstituteCredit, String> {
    Optional<InstituteCredit> findByInstituteIdentifier(String instituteIdentifier);
    boolean existsByInstituteIdentifier(String instituteIdentifier);
}
