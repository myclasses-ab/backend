package com.classes.Backend.Domain.institute;

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
@Table(name = "institute_facilities")
public class InstituteFacility {

    @Id
    @Column(name = "identifier", unique = true)
    private String identifier = UUID.randomUUID().toString();

    @Column(name = "institute_identifier")
    private String instituteIdentifier;

    @Column(name = "has_library")
    private Boolean hasLibrary = false;

    @Column(name = "has_hostel")
    private Boolean hasHostel = false;

    @Column(name = "has_canteen")
    private Boolean hasCanteen = false;

    @Column(name = "has_transport")
    private Boolean hasTransport = false;

    @Column(name = "has_ac_classrooms")
    private Boolean hasAcClassrooms = false;

    @Column(name = "has_digital_boards")
    private Boolean hasDigitalBoards = false;

    @Column(name = "has_laboratory")
    private Boolean hasLaboratory = false;

    @Column(name = "has_study_room")
    private Boolean hasStudyRoom = false;

    @Column(name = "has_wifi")
    private Boolean hasWifi = false;

    @Column(name = "has_cctv")
    private Boolean hasCctv = false;

    @Column(name = "has_online_portal")
    private Boolean hasOnlinePortal = false;

    @Column(name = "has_doubt_sessions")
    private Boolean hasDoubtSessions = false;

    @Column(name = "has_mock_test_series")
    private Boolean hasMockTestSeries = false;

    @Column(name = "has_study_material")
    private Boolean hasStudyMaterial = false;

    @Column(name = "has_crash_courses")
    private Boolean hasCrashCourses = false;

    @Column(name = "has_scholarship_program")
    private Boolean hasScholarshipProgram = false;

    @Column(name = "has_free_demo_class")
    private Boolean hasFreeDemoClass = false;

    @Column(name = "has_parent_teacher_meetings")
    private Boolean hasParentTeacherMeetings = false;

    @Column(name = "has_performance_tracking")
    private Boolean hasPerformanceTracking = false;

    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
