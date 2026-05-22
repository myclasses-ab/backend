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
@Table(name = "awards_and_recognitions")
public class AwardAndRecognition {

    @Id
    @Column(name = "identifier", unique = true)
    private String identifier = UUID.randomUUID().toString();

    @Column(name = "institute_identifier")
    private String instituteIdentifier;

    @Column(name = "title", length = 500, nullable = false)
    private String title;

    @Column(name = "issuing_body", length = 300)
    private String issuingBody;

    @Column(name = "year")
    private Integer year;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "certificate_url", length = 500)
    private String certificateUrl;

    @Column(name = "is_verified")
    private Boolean isVerified = false;

    @Column(name = "display_order")
    private Integer displayOrder;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}
