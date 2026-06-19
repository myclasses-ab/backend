package com.classes.Backend.Domain.institute;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Table(name = "branches")
public class Branch {

    @Id
    @Column(name = "identifier", unique = true)
    private String identifier = UUID.randomUUID().toString();

    @Column(name = "institute_identifier")
    private String instituteIdentifier;

    @Column(name = "name", length = 300)
    private String name;

    @Column(name = "is_main_branch")
    private Boolean isMainBranch = false;

    @Column(name = "is_online_only")
    private Boolean isOnlineOnly = false;

    @Column(name = "address", length = 1000)
    private String address;

    @Column(name = "landmark", length = 300)
    private String landmark;

    @Column(name = "city_identifier")
    private String cityIdentifier;

    @Column(name = "city_name", length = 200)
    private String cityName;

    @Column(name = "state", length = 200)
    private String state;

    @Column(name = "pincode", length = 10)
    private String pincode;

    @Column(name = "latitude", precision = 10, scale = 7)
    private BigDecimal latitude;

    @Column(name = "longitude", precision = 10, scale = 7)
    private BigDecimal longitude;

    @Column(name = "google_maps_url", length = 1000)
    private String googleMapsUrl;

    @Column(name = "phone", length = 20)
    private String phone;

    @Column(name = "email", length = 255)
    private String email;

    @Column(name = "operating_hours_start")
    private LocalTime operatingHoursStart;

    @Column(name = "operating_hours_end")
    private LocalTime operatingHoursEnd;

    @Column(name = "operating_days", length = 100)
    private String operatingDays;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}
