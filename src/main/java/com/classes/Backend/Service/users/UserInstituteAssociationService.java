package com.classes.Backend.Service.users;

import com.classes.Backend.Domain.users.UserInstituteAssociation;
import com.classes.Backend.Domain.enums.InstituteStaffRole;

import java.util.List;
import java.util.Optional;

public interface UserInstituteAssociationService {
    // ================ CRUD OPERATIONS ===================== //
    UserInstituteAssociation save(UserInstituteAssociation userInstituteAssociation);
    List<UserInstituteAssociation> saveAll(List<UserInstituteAssociation> userInstituteAssociations);
    Optional<UserInstituteAssociation> findById(String identifier);
    List<UserInstituteAssociation> findAll();
    void deleteById(String identifier);
    boolean existsById(String identifier);

    // ================ CUSTOM FINDER METHODS ===================== //
    List<UserInstituteAssociation> findByUserIdentifier(String userIdentifier);
    List<UserInstituteAssociation> findByInstituteIdentifier(String instituteIdentifier);
    Optional<UserInstituteAssociation> findByUserIdentifierAndInstituteIdentifier(String userIdentifier, String instituteIdentifier);
    List<UserInstituteAssociation> findByRole(InstituteStaffRole role);
    List<UserInstituteAssociation> findByIsActiveTrue();
}
