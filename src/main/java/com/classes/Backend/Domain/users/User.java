package com.classes.Backend.Domain.users;

import com.classes.Backend.Domain.enums.Standard;
import com.classes.Backend.Domain.enums.UserRole;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Table(name = "users")
public class User {

    @Id
    @Column(name = "identifier", unique = true)
    private String identifier = UUID.randomUUID().toString();

    @Column(name = "full_name", length = 200)
    private String fullName;

    @Column(name = "email", length = 255, unique = true)
    private String email;

    @Column(name = "phone", length = 20, unique = true)
    private String phone;

    @Column(name = "phone_verified")
    private Boolean phoneVerified = false;

    @Column(name = "email_verified")
    private Boolean emailVerified = false;

    @Column(name = "password_hash", length = 500)
    private String passwordHash;

    @Column(name = "avatar_url", length = 500)
    private String avatarUrl;

    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    private UserRole role;

    @Enumerated(EnumType.STRING)
    @Column(name = "current_standard")
    private Standard currentStandard;

    @ElementCollection
    @CollectionTable(name = "user_target_exams", joinColumns = @JoinColumn(name = "user_identifier"))
    @Column(name = "exam_type_identifier")
    private List<String> targetExamIdentifiers;

    @Column(name = "city_identifier")
    private String cityIdentifier;

    @Column(name = "state", length = 200)
    private String state;

    @Column(name = "pincode", length = 10)
    private String pincode;

    @Column(name = "school_college_name", length = 300)
    private String schoolCollegeName;

    @Column(name = "preferred_language", length = 50)
    private String preferredLanguage = "English";

    @ElementCollection
    @CollectionTable(name = "user_searched_cities", joinColumns = @JoinColumn(name = "user_identifier"))
    @Column(name = "city_name")
    private List<String> searchedCities;

    @ElementCollection
    @CollectionTable(name = "user_searched_exams", joinColumns = @JoinColumn(name = "user_identifier"))
    @Column(name = "exam_name")
    private List<String> searchedExams;

    @ElementCollection
    @CollectionTable(name = "user_visited_institutes", joinColumns = @JoinColumn(name = "user_identifier"))
    @Column(name = "institute_identifier")
    private List<String> visitedInstituteIdentifiers;

    @ElementCollection
    @CollectionTable(name = "user_visited_institute_names", joinColumns = @JoinColumn(name = "user_identifier"))
    @Column(name = "institute_name")
    private List<String> visitedInstituteNames;

    @Column(name = "is_active")
    private Boolean isActive = true;

    @Column(name = "last_login_at")
    private LocalDateTime lastLoginAt;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
