package com.classes.Backend.Repository.subscription;

import com.classes.Backend.Domain.subscription.InstituteCredit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface InstituteCreditRepository extends JpaRepository<InstituteCredit, String> {
    Optional<InstituteCredit> findByInstituteIdentifier(String instituteIdentifier);
    boolean existsByInstituteIdentifier(String instituteIdentifier);
}
