package com.classes.Backend.Repository.institute;

import com.classes.Backend.Domain.institute.InstituteFacility;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface InstituteFacilityRepository extends JpaRepository<InstituteFacility, String> {
    Optional<InstituteFacility> findByInstituteIdentifier(String instituteIdentifier);

    List<InstituteFacility> findByInstituteIdentifierIn(List<String> instituteIdentifiers);
}
