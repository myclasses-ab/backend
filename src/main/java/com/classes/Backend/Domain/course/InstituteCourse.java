package com.classes.Backend.Domain.course;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Table(name = "institute_courses")
public class InstituteCourse {

    @Id
    @Column(name = "identifier", unique = true)
    private String identifier = UUID.randomUUID().toString();

    @Column(name = "institute_identifier")
    private String instituteIdentifier;

    @Column(name = "branch_identifier")
    private String branchIdentifier;

    @Column(name = "custom_name", length = 300)
    private String courseName;

    @Column(name = "fee", precision = 12, scale = 2)
    private BigDecimal fee;

    @Column(name = "scholarship_available")
    private Boolean scholarshipAvailable = false;

    @Column(name = "scholarship_details", columnDefinition = "TEXT")
    private String scholarshipDetails;

    @Column(name = "duration_months")
    private Integer durationMonths;

    @Column(name = "study_material_included")
    private Boolean studyMaterialIncluded = true;

    @Column(name = "test_series_included")
    private Boolean testSeriesIncluded = true;

    @Column(name = "recorded_lectures_available")
    private Boolean recordedLecturesAvailable = false;

    @Column(name = "admission_open")
    private Boolean admissionOpen = true;

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
