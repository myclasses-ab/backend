package com.classes.Backend.Domain.faculty;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Table(name = "faculty")
public class Faculty {

    @Id
    @Column(name = "identifier", unique = true)
    private String identifier = UUID.randomUUID().toString();

    @Column(name = "institute_identifier")
    private String instituteIdentifier;

    @Column(name = "name", length = 200, nullable = false)
    private String name;

    @Column(name = "photo_url", length = 500)
    private String photoUrl;

    @Column(name = "designation", length = 200)
    private String designation;

    @Column(name = "qualification", length = 500)
    private String qualification;

    @Column(name = "experience_years")
    private Integer experienceYears;

    @Column(name = "bio", columnDefinition = "TEXT")
    private String bio;

    @Column(name = "specialization", columnDefinition = "TEXT")
    private String specialization;

    @Column(name = "iit_iim_background")
    private Boolean iitIimBackground = false;

    @Column(name = "nit_background")
    private Boolean nitBackground = false;

    @Column(name = "achievements", columnDefinition = "TEXT")
    private String achievements;

    @Column(name = "former_institutes", columnDefinition = "TEXT")
    private String formerInstitutes;

    @Column(name = "student_rating", precision = 3, scale = 2)
    private BigDecimal studentRating;

    @Column(name = "is_active")
    private Boolean isActive = true;

    @Column(name = "display_order")
    private Integer displayOrder;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @ElementCollection
    @CollectionTable(name = "faculty_subjects", joinColumns = @JoinColumn(name = "faculty_identifier"))
    @Column(name = "subject_identifier")
    private List<String> subjectIdentifiers;

    @ElementCollection
    @CollectionTable(name = "faculty_exam_types", joinColumns = @JoinColumn(name = "faculty_identifier"))
    @Column(name = "exam_type_identifier")
    private List<String> examTypeIdentifiers;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}
