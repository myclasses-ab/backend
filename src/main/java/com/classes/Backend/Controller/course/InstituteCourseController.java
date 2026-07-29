package com.classes.Backend.Controller.course;

import com.classes.Backend.Domain.activity.ActivityActionType;
import com.classes.Backend.Domain.activity.ActivityEntityType;
import com.classes.Backend.Domain.course.InstituteCourse;
import com.classes.Backend.Service.activity.ActivityLogActorResolver;
import com.classes.Backend.Service.activity.ActivityLogChangeExtractor;
import com.classes.Backend.Service.activity.ActivityLogService;
import com.classes.Backend.Service.activity.ResolvedActor;
import com.classes.Backend.Service.course.InstituteCourseServiceImpl;
import com.classes.Backend.dto.activity.ActivityLogRequest;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/institute-courses")
public class InstituteCourseController {

    private final InstituteCourseServiceImpl INSTITUTE_COURSE_SERVICE_IMPL;
    private final ActivityLogService ACTIVITY_LOG_SERVICE;
    private final ActivityLogActorResolver ACTOR_RESOLVER;

    // ================ CREATE INSTITUTE COURSE ===================== //
    @PostMapping
    public ResponseEntity<?> saveInstituteCourse(@RequestBody InstituteCourse instituteCourse, HttpServletRequest request) {
        InstituteCourse saved = this.INSTITUTE_COURSE_SERVICE_IMPL.save(instituteCourse);

        ResolvedActor actor = ACTOR_RESOLVER.resolve(request);
        if (actor.isAuthenticated()) {
            ACTIVITY_LOG_SERVICE.log(ActivityLogRequest.builder()
                    .actorType(actor.getType())
                    .actorIdentifier(actor.getIdentifier())
                    .actorName(actor.getName())
                    .actionType(ActivityActionType.COURSE_CREATED)
                    .entityType(ActivityEntityType.COURSE)
                    .entityIdentifier(saved.getIdentifier())
                    .entityName(saved.getCourseName())
                    .instituteIdentifier(saved.getInstituteIdentifier())
                    .description("Created course" + (saved.getCourseName() != null ? " " + saved.getCourseName() : ""))
                    .source("CONSOLE")
                    .build());
        }

        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }

    // ================ CREATE ALL INSTITUTE COURSES ===================== //
    @PostMapping("/bulk")
    public ResponseEntity<?> saveAllInstituteCourses(@RequestBody List<InstituteCourse> instituteCourses) {
        return new ResponseEntity<>(this.INSTITUTE_COURSE_SERVICE_IMPL.saveAll(instituteCourses), HttpStatus.CREATED);
    }

    // ================ GET INSTITUTE COURSE BY ID ===================== //
    @GetMapping("/{identifier}")
    public ResponseEntity<?> getInstituteCourseById(@PathVariable String identifier) {
        return new ResponseEntity<>(this.INSTITUTE_COURSE_SERVICE_IMPL.findById(identifier), HttpStatus.OK);
    }

    // ================ GET ALL INSTITUTE COURSES ===================== //
    @GetMapping
    public ResponseEntity<?> getAllInstituteCourses() {
        List<InstituteCourse> allInstituteCourses = this.INSTITUTE_COURSE_SERVICE_IMPL.findAll();
        return new ResponseEntity<>(allInstituteCourses, HttpStatus.OK);
    }

    // ================ DELETE INSTITUTE COURSE BY ID ===================== //
    @DeleteMapping("/{identifier}")
    public ResponseEntity<?> deleteInstituteCourseById(@PathVariable String identifier, HttpServletRequest request) {
        InstituteCourse existing = this.INSTITUTE_COURSE_SERVICE_IMPL.findById(identifier).orElse(null);

        this.INSTITUTE_COURSE_SERVICE_IMPL.deleteById(identifier);

        ResolvedActor actor = ACTOR_RESOLVER.resolve(request);
        if (actor.isAuthenticated() && existing != null) {
            ACTIVITY_LOG_SERVICE.log(ActivityLogRequest.builder()
                    .actorType(actor.getType())
                    .actorIdentifier(actor.getIdentifier())
                    .actorName(actor.getName())
                    .actionType(ActivityActionType.COURSE_DELETED)
                    .entityType(ActivityEntityType.COURSE)
                    .entityIdentifier(identifier)
                    .entityName(existing.getCourseName())
                    .instituteIdentifier(existing.getInstituteIdentifier())
                    .description("Deleted course" + (existing.getCourseName() != null ? " " + existing.getCourseName() : ""))
                    .source("CONSOLE")
                    .build());
        }

        return new ResponseEntity<>("InstituteCourse deleted successfully", HttpStatus.OK);
    }

    // ================ UPDATE INSTITUTECOURSE BY ID ===================== //
    @PutMapping("/{identifier}")
    public ResponseEntity<?> updateInstituteCourseById(@PathVariable String identifier, @RequestBody InstituteCourse instituteCourse, HttpServletRequest request) {
        InstituteCourse existing = this.INSTITUTE_COURSE_SERVICE_IMPL.findById(identifier).orElse(null);
        if (existing == null) {
            return new ResponseEntity<>("InstituteCourse not found", HttpStatus.NOT_FOUND);
        }
        instituteCourse.setIdentifier(identifier);
        InstituteCourse updated = this.INSTITUTE_COURSE_SERVICE_IMPL.save(instituteCourse);

        ResolvedActor actor = ACTOR_RESOLVER.resolve(request);
        if (actor.isAuthenticated()) {
            ACTIVITY_LOG_SERVICE.log(ActivityLogRequest.builder()
                    .actorType(actor.getType())
                    .actorIdentifier(actor.getIdentifier())
                    .actorName(actor.getName())
                    .actionType(ActivityActionType.COURSE_UPDATED)
                    .entityType(ActivityEntityType.COURSE)
                    .entityIdentifier(updated.getIdentifier())
                    .entityName(updated.getCourseName())
                    .instituteIdentifier(updated.getInstituteIdentifier())
                    .description("Updated course" + (updated.getCourseName() != null ? " " + updated.getCourseName() : ""))
                    .metadata(Map.of("changedFields", ActivityLogChangeExtractor.extractChangedFields(existing, updated)))
                    .source("CONSOLE")
                    .build());
        }

        return new ResponseEntity<>(updated, HttpStatus.OK);
    }

    // ================ FIND BY INSTITUTE IDENTIFIER ===================== //
    @GetMapping("/institute/{instituteIdentifier}")
    public ResponseEntity<?> findByInstituteIdentifier(@PathVariable String instituteIdentifier) {
        return new ResponseEntity<>(this.INSTITUTE_COURSE_SERVICE_IMPL.findByInstituteIdentifier(instituteIdentifier), HttpStatus.OK);
    }

    // ================ FIND BY BRANCH IDENTIFIER ===================== //
    @GetMapping("/branch/{branchIdentifier}")
    public ResponseEntity<?> findByBranchIdentifier(@PathVariable String branchIdentifier) {
        return new ResponseEntity<>(this.INSTITUTE_COURSE_SERVICE_IMPL.findByBranchIdentifier(branchIdentifier), HttpStatus.OK);
    }

    // ================ FIND BY ADMISSION OPEN TRUE ===================== //
    @GetMapping("/admission-open")
    public ResponseEntity<?> findByAdmissionOpenTrue() {
        return new ResponseEntity<>(this.INSTITUTE_COURSE_SERVICE_IMPL.findByAdmissionOpenTrue(), HttpStatus.OK);
    }

}
