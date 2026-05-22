package com.classes.Backend.Service.users;

import com.classes.Backend.Domain.users.UserInstituteAssociation;
import com.classes.Backend.Domain.enums.InstituteStaffRole;
import com.classes.Backend.Repository.users.UserInstituteAssociationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserInstituteAssociationServiceImpl implements UserInstituteAssociationService {
    private final UserInstituteAssociationRepository USER_INSTITUTE_ASSOCIATION_REPOSITORY;

    // ================ SAVE USER INSTITUTE ASSOCIATION ===================== //
    @Override
    public UserInstituteAssociation save(UserInstituteAssociation userInstituteAssociation) {
        return this.USER_INSTITUTE_ASSOCIATION_REPOSITORY.save(userInstituteAssociation);
    }

    // ================ SAVE ALL USER INSTITUTE ASSOCIATIONS ===================== //
    @Override
    public List<UserInstituteAssociation> saveAll(List<UserInstituteAssociation> userInstituteAssociations) {
        return this.USER_INSTITUTE_ASSOCIATION_REPOSITORY.saveAll(userInstituteAssociations);
    }

    // ================ FIND BY ID ===================== //
    @Override
    public Optional<UserInstituteAssociation> findById(String identifier) {
        return this.USER_INSTITUTE_ASSOCIATION_REPOSITORY.findById(identifier);
    }

    // ================ FIND ALL ===================== //
    @Override
    public List<UserInstituteAssociation> findAll() {
        return this.USER_INSTITUTE_ASSOCIATION_REPOSITORY.findAll();
    }

    // ================ DELETE BY ID ===================== //
    @Override
    public void deleteById(String identifier) {
        if (!this.USER_INSTITUTE_ASSOCIATION_REPOSITORY.existsById(identifier)) {
            throw new RuntimeException("UserInstituteAssociation with identifier '" + identifier + "' not found");
        }
        this.USER_INSTITUTE_ASSOCIATION_REPOSITORY.deleteById(identifier);
    }

    // ================ EXISTS BY ID ===================== //
    @Override
    public boolean existsById(String identifier) {
        return this.USER_INSTITUTE_ASSOCIATION_REPOSITORY.existsById(identifier);
    }

    // ================ FIND BY USER IDENTIFIER ===================== //
    @Override
    public List<UserInstituteAssociation> findByUserIdentifier(String userIdentifier) {
        return this.USER_INSTITUTE_ASSOCIATION_REPOSITORY.findByUserIdentifier(userIdentifier);
    }

    // ================ FIND BY INSTITUTE IDENTIFIER ===================== //
    @Override
    public List<UserInstituteAssociation> findByInstituteIdentifier(String instituteIdentifier) {
        return this.USER_INSTITUTE_ASSOCIATION_REPOSITORY.findByInstituteIdentifier(instituteIdentifier);
    }

    // ================ FIND BY USER IDENTIFIER AND INSTITUTE IDENTIFIER ===================== //
    @Override
    public Optional<UserInstituteAssociation> findByUserIdentifierAndInstituteIdentifier(String userIdentifier, String instituteIdentifier) {
        return this.USER_INSTITUTE_ASSOCIATION_REPOSITORY.findByUserIdentifierAndInstituteIdentifier(userIdentifier, instituteIdentifier);
    }

    // ================ FIND BY ROLE ===================== //
    @Override
    public List<UserInstituteAssociation> findByRole(InstituteStaffRole role) {
        return this.USER_INSTITUTE_ASSOCIATION_REPOSITORY.findByRole(role);
    }

    // ================ FIND BY IS ACTIVE TRUE ===================== //
    @Override
    public List<UserInstituteAssociation> findByIsActiveTrue() {
        return this.USER_INSTITUTE_ASSOCIATION_REPOSITORY.findByIsActiveTrue();
    }
}
