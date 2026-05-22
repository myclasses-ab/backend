package com.classes.Backend.Service.users;

import com.classes.Backend.Domain.users.User;
import com.classes.Backend.Domain.enums.UserRole;
import com.classes.Backend.Repository.users.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {
    private final UserRepository USER_REPOSITORY;

    // ================ SAVE USER ===================== //
    @Override
    public User save(User user) {
        return this.USER_REPOSITORY.save(user);
    }

    // ================ SAVE ALL USERS ===================== //
    @Override
    public List<User> saveAll(List<User> users) {
        return this.USER_REPOSITORY.saveAll(users);
    }

    // ================ FIND BY ID ===================== //
    @Override
    public Optional<User> findById(String identifier) {
        return this.USER_REPOSITORY.findById(identifier);
    }

    // ================ FIND ALL ===================== //
    @Override
    public List<User> findAll() {
        return this.USER_REPOSITORY.findAll();
    }

    // ================ DELETE BY ID ===================== //
    @Override
    public void deleteById(String identifier) {
        if (!this.USER_REPOSITORY.existsById(identifier)) {
            throw new RuntimeException("User with identifier '" + identifier + "' not found");
        }
        this.USER_REPOSITORY.deleteById(identifier);
    }

    // ================ EXISTS BY ID ===================== //
    @Override
    public boolean existsById(String identifier) {
        return this.USER_REPOSITORY.existsById(identifier);
    }

    // ================ FIND BY EMAIL ===================== //
    @Override
    public Optional<User> findByEmail(String email) {
        return this.USER_REPOSITORY.findByEmail(email);
    }

    // ================ FIND BY PHONE ===================== //
    @Override
    public Optional<User> findByPhone(String phone) {
        return this.USER_REPOSITORY.findByPhone(phone);
    }

    // ================ FIND BY ROLE ===================== //
    @Override
    public List<User> findByRole(UserRole role) {
        return this.USER_REPOSITORY.findByRole(role);
    }

    // ================ FIND BY CITY IDENTIFIER ===================== //
    @Override
    public List<User> findByCityIdentifier(String cityIdentifier) {
        return this.USER_REPOSITORY.findByCityIdentifier(cityIdentifier);
    }

    // ================ FIND BY IS ACTIVE TRUE ===================== //
    @Override
    public List<User> findByIsActiveTrue() {
        return this.USER_REPOSITORY.findByIsActiveTrue();
    }

    // ================ EXISTS BY EMAIL ===================== //
    @Override
    public boolean existsByEmail(String email) {
        return this.USER_REPOSITORY.existsByEmail(email);
    }

    // ================ EXISTS BY PHONE ===================== //
    @Override
    public boolean existsByPhone(String phone) {
        return this.USER_REPOSITORY.existsByPhone(phone);
    }
}
