package com.classes.Backend.Domain.users;

import com.classes.Backend.Domain.enums.InstituteStaffRole;
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
@Table(name = "user_institute_associations", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"user_identifier", "institute_identifier"})
})
public class UserInstituteAssociation {

    @Id
    @Column(name = "identifier", unique = true)
    private String identifier = UUID.randomUUID().toString();

    @Column(name = "user_identifier")
    private String userIdentifier;

    @Column(name = "institute_identifier")
    private String instituteIdentifier;

    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    private InstituteStaffRole role;

    @Column(name = "is_active")
    private Boolean isActive = true;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}
