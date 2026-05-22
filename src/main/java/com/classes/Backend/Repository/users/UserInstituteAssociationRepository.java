package com.classes.Backend.Repository.users;

import com.classes.Backend.Domain.users.UserInstituteAssociation;
import com.classes.Backend.Domain.enums.InstituteStaffRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserInstituteAssociationRepository extends JpaRepository<UserInstituteAssociation, String> {
    List<UserInstituteAssociation> findByUserIdentifier(String userIdentifier);
    List<UserInstituteAssociation> findByInstituteIdentifier(String instituteIdentifier);
    Optional<UserInstituteAssociation> findByUserIdentifierAndInstituteIdentifier(String userIdentifier, String instituteIdentifier);
    List<UserInstituteAssociation> findByRole(InstituteStaffRole role);
    List<UserInstituteAssociation> findByIsActiveTrue();
}
