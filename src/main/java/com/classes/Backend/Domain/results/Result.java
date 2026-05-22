package com.classes.Backend.Domain.results;

import com.classes.Backend.Domain.enums.RankOrScoreType;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Table(name = "results")
public class Result {

    @Id
    @Column(name = "identifier", unique = true)
    private String identifier = UUID.randomUUID().toString();

    @Column(name = "institute_identifier")
    private String instituteIdentifier;

    @Column(name = "exam_type_identifier")
    private String examTypeIdentifier;

    @Column(name = "exam_year", nullable = false)
    private Integer examYear;

    @Column(name = "student_name", length = 200)
    private String studentName;

    @Column(name = "student_photo_url", length = 500)
    private String studentPhotoUrl;

    @Enumerated(EnumType.STRING)
    @Column(name = "rank_or_score_type")
    private RankOrScoreType rankOrScoreType;

    @Column(name = "value", length = 100)
    private String value;

    @Column(name = "college_admitted", length = 300)
    private String collegeAdmitted;

    @Column(name = "testimonial_quote", columnDefinition = "TEXT")
    private String testimonialQuote;

    @Column(name = "is_verified")
    private Boolean isVerified = false;

    @Column(name = "is_featured")
    private Boolean isFeatured = false;

    @Column(name = "display_order")
    private Integer displayOrder;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}
