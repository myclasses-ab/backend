package com.classes.Backend.Domain.results;

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

    @Column(name = "exam", length = 200)
    private String exam;

    @Column(name = "student_name", length = 200)
    private String studentName;

    @Column(name = "student_photo_url", length = 500)
    private String studentPhotoUrl;

    @Column(name = "value", length = 100)
    private String value;

    @Column(name = "testimonial_quote", columnDefinition = "TEXT")
    private String testimonialQuote;

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
