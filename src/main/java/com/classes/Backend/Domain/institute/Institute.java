package com.classes.Backend.Domain.institute;

import com.classes.Backend.Domain.course.InstituteCourse;
import com.classes.Backend.Domain.enums.InstituteType;
import com.classes.Backend.Domain.enums.OwnershipType;
import com.classes.Backend.Domain.enums.SubscriptionTier;
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
@Table(name = "institutes")
public class Institute {

    @Id
    @Column(name = "identifier", unique = true)
    private String identifier;

    @Column(name = "name", length = 300, nullable = false)
    private String name;

    @Column(name = "slug", length = 300, unique = true, nullable = false)
    private String slug;

    @Column(name = "tagline", length = 500)
    private String tagline;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "founded_year")
    private Integer foundedYear;

    @Column(name = "logo_url", length = 500)
    private String logoUrl;

    @Column(name = "banner_url", length = 500)
    private String bannerUrl;

    @Column(name = "website_url", length = 500)
    private String websiteUrl;

    @Column(name = "email", length = 255)
    private String email;

    @Column(name = "phone_primary", length = 20)
    private String phonePrimary;

    @Column(name = "whatsapp_number", length = 20)
    private String whatsappNumber;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private InstituteType type = InstituteType.OFFLINE;

    @Enumerated(EnumType.STRING)
    @Column(name = "ownership_type")
    private OwnershipType ownershipType;

    @Column(name = "is_franchise")
    private Boolean isFranchise = false;

    @Column(name = "parent_institute_identifier")
    private String parentInstituteIdentifier;

    @Column(name = "average_rating", precision = 3, scale = 2)
    private BigDecimal averageRating;

    @Column(name = "total_reviews")
    private Integer totalReviews = 0;

    @Column(name = "total_students_enrolled")
    private Integer totalStudentsEnrolled;

    @Column(name = "years_of_experience")
    private Integer yearsOfExperience;

    @Column(name = "is_verified")
    private Boolean isVerified = false;

    @Column(name = "is_featured")
    private Boolean isFeatured = false;

    @Column(name = "is_active")
    private Boolean isActive = true;

    @Enumerated(EnumType.STRING)
    @Column(name = "subscription_tier")
    private SubscriptionTier subscriptionTier = SubscriptionTier.FREE;

    @Column(name = "meta_title", length = 300)
    private String metaTitle;

    @Column(name = "meta_description", length = 500)
    private String metaDescription;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "created_by")
    private String createdBy;

    @Transient
    private InstituteFacility facilities;

    @Transient
    private List<InstituteCourse> matchingCourses;

    @Transient
    private List<String> starredMediaUrls;

    @PrePersist
    protected void onCreate() {
        // Auto-generate UUID identifier if not set (safety net)
        if (identifier == null || identifier.isBlank()) {
            identifier = UUID.randomUUID().toString();
        }
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
