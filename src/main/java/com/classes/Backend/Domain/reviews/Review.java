package com.classes.Backend.Domain.reviews;

import com.classes.Backend.Domain.enums.Standard;
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
@Table(name = "reviews")
public class Review {

    @Id
    @Column(name = "identifier", unique = true)
    private String identifier = UUID.randomUUID().toString();

    @Column(name = "institute_identifier")
    private String instituteIdentifier;

    @Column(name = "user_identifier")
    private String userIdentifier;

    @Column(name = "course_taken", length = 200)
    private String courseTaken;

    @Enumerated(EnumType.STRING)
    @Column(name = "standard_when_enrolled")
    private Standard standardWhenEnrolled;

    @Column(name = "review_title", length = 300)
    private String reviewTitle;

    @Column(name = "review_text", columnDefinition = "TEXT")
    private String reviewText;

    @Column(name = "overall_rating", precision = 3, scale = 2)
    private BigDecimal overallRating;

    @Column(name = "faculty_rating", precision = 3, scale = 2)
    private BigDecimal facultyRating;

    @Column(name = "study_material_rating", precision = 3, scale = 2)
    private BigDecimal studyMaterialRating;

    @Column(name = "infrastructure_rating", precision = 3, scale = 2)
    private BigDecimal infrastructureRating;

    @Column(name = "fee_value_rating", precision = 3, scale = 2)
    private BigDecimal feeValueRating;

    @Column(name = "online_support_rating", precision = 3, scale = 2)
    private BigDecimal onlineSupportRating;

    @Column(name = "result_achievement_rating", precision = 3, scale = 2)
    private BigDecimal resultAchievementRating;

    @Column(name = "reported_count")
    private Integer reportedCount = 0;

    @Column(name = "is_verified_student")
    private Boolean isVerifiedStudent = false;

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
