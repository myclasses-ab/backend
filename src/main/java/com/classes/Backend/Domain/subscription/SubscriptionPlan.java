package com.classes.Backend.Domain.subscription;

import com.classes.Backend.Domain.enums.SubscriptionTier;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Table(name = "subscription_plans")
public class SubscriptionPlan {

    @Id
    @Column(name = "identifier", unique = true)
    private String identifier = UUID.randomUUID().toString();

    @Enumerated(EnumType.STRING)
    @Column(name = "name")
    private SubscriptionTier name;

    @Column(name = "price_monthly", precision = 10, scale = 2)
    private BigDecimal priceMonthly;

    @Column(name = "price_yearly", precision = 10, scale = 2)
    private BigDecimal priceYearly;

    @Column(name = "max_branches")
    private Integer maxBranches;

    @Column(name = "max_courses")
    private Integer maxCourses;

    @Column(name = "max_faculty")
    private Integer maxFaculty;

    @Column(name = "max_media_uploads")
    private Integer maxMediaUploads;

    @Column(name = "can_respond_to_reviews")
    private Boolean canRespondToReviews = false;

    @Column(name = "can_view_leads")
    private Boolean canViewLeads = false;

    @Column(name = "can_feature_results")
    private Boolean canFeatureResults = false;

    @Column(name = "priority_in_search")
    private Integer priorityInSearch;

    @Column(name = "badge_shown", length = 100)
    private String badgeShown;

    @Column(name = "is_active")
    private Boolean isActive = true;
}
