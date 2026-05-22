package com.classes.Backend.Domain.leads;

import com.classes.Backend.Domain.enums.LeadSource;
import com.classes.Backend.Domain.enums.LeadStatus;
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
@Table(name = "leads")
public class Lead {

    @Id
    @Column(name = "identifier", unique = true)
    private String identifier = UUID.randomUUID().toString();

    @Column(name = "user_identifier")
    private String userIdentifier;

    @Column(name = "phone", length = 20)
    private String phone;

    @Column(name = "full_name", length = 200)
    private String fullName;

    @Column(name = "city_identifier")
    private String cityIdentifier;

    @Column(name = "exam_type_identifier")
    private String examTypeIdentifier;

    @Column(name = "searched_query", length = 500)
    private String searchedQuery;

    @Column(name = "visited_institute_identifier")
    private String visitedInstituteIdentifier;

    @Column(name = "visited_institute_name", length = 300)
    private String visitedInstituteName;

    @Enumerated(EnumType.STRING)
    @Column(name = "source")
    private LeadSource source;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private LeadStatus status = LeadStatus.NEW;

    @Column(name = "is_active")
    private Boolean isActive = true;

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
