package com.classes.Backend.Repository.users;

import com.classes.Backend.Domain.users.User;
import com.classes.Backend.Domain.enums.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String> {
    Optional<User> findByEmail(String email);
    Optional<User> findByPhone(String phone);
    List<User> findByRole(UserRole role);
    List<User> findByCityIdentifier(String cityIdentifier);
    List<User> findByIsActiveTrue();
    boolean existsByEmail(String email);
    boolean existsByPhone(String phone);
}
