package com.classes.Backend.Service.users;

import com.classes.Backend.Domain.users.User;
import com.classes.Backend.Domain.enums.UserRole;

import java.util.List;
import java.util.Optional;

public interface UserService {
    // ================ CRUD OPERATIONS ===================== //
    User save(User user);
    List<User> saveAll(List<User> users);
    Optional<User> findById(String identifier);
    List<User> findAll();
    void deleteById(String identifier);
    boolean existsById(String identifier);

    // ================ CUSTOM FINDER METHODS ===================== //
    Optional<User> findByEmail(String email);
    Optional<User> findByPhone(String phone);
    List<User> findByRole(UserRole role);
    List<User> findByCityIdentifier(String cityIdentifier);
    List<User> findByIsActiveTrue();
    boolean existsByEmail(String email);
    boolean existsByPhone(String phone);
}
