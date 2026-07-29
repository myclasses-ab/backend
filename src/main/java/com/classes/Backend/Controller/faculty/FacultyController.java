package com.classes.Backend.Controller.faculty;

import com.classes.Backend.Domain.activity.ActivityActionType;
import com.classes.Backend.Domain.activity.ActivityEntityType;
import com.classes.Backend.Domain.faculty.Faculty;
import com.classes.Backend.Service.activity.ActivityLogActorResolver;
import com.classes.Backend.Service.activity.ActivityLogChangeExtractor;
import com.classes.Backend.Service.activity.ActivityLogService;
import com.classes.Backend.Service.activity.ResolvedActor;
import com.classes.Backend.Service.faculty.FacultyServiceImpl;
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
@RequestMapping("/api/faculty")
public class FacultyController {

    private final FacultyServiceImpl FACULTY_SERVICE_IMPL;
    private final ActivityLogService ACTIVITY_LOG_SERVICE;
    private final ActivityLogActorResolver ACTOR_RESOLVER;

    // ================ CREATE FACULTY ===================== //
    @PostMapping
    public ResponseEntity<?> saveFaculty(@RequestBody Faculty faculty, HttpServletRequest request) {
        Faculty saved = this.FACULTY_SERVICE_IMPL.save(faculty);

        ResolvedActor actor = ACTOR_RESOLVER.resolve(request);
        if (actor.isAuthenticated()) {
            ACTIVITY_LOG_SERVICE.log(ActivityLogRequest.builder()
                    .actorType(actor.getType())
                    .actorIdentifier(actor.getIdentifier())
                    .actorName(actor.getName())
                    .actionType(ActivityActionType.FACULTY_CREATED)
                    .entityType(ActivityEntityType.FACULTY)
                    .entityIdentifier(saved.getIdentifier())
                    .entityName(saved.getName())
                    .instituteIdentifier(saved.getInstituteIdentifier())
                    .description("Created faculty" + (saved.getName() != null ? " " + saved.getName() : ""))
                    .source("CONSOLE")
                    .build());
        }

        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }

    // ================ CREATE ALL FACULTY ===================== //
    @PostMapping("/bulk")
    public ResponseEntity<?> saveAllFaculty(@RequestBody List<Faculty> facultyList) {
        return new ResponseEntity<>(this.FACULTY_SERVICE_IMPL.saveAll(facultyList), HttpStatus.CREATED);
    }

    // ================ GET FACULTY BY ID ===================== //
    @GetMapping("/{identifier}")
    public ResponseEntity<?> getFacultyById(@PathVariable String identifier) {
        return new ResponseEntity<>(this.FACULTY_SERVICE_IMPL.findById(identifier), HttpStatus.OK);
    }

    // ================ GET ALL FACULTY ===================== //
    @GetMapping
    public ResponseEntity<?> getAllFaculty() {
        List<Faculty> allFaculty = this.FACULTY_SERVICE_IMPL.findAll();
        return new ResponseEntity<>(allFaculty, HttpStatus.OK);
    }

    // ================ DELETE FACULTY BY ID ===================== //
    @DeleteMapping("/{identifier}")
    public ResponseEntity<?> deleteFacultyById(@PathVariable String identifier, HttpServletRequest request) {
        Faculty existing = this.FACULTY_SERVICE_IMPL.findById(identifier).orElse(null);

        this.FACULTY_SERVICE_IMPL.deleteById(identifier);

        ResolvedActor actor = ACTOR_RESOLVER.resolve(request);
        if (actor.isAuthenticated() && existing != null) {
            ACTIVITY_LOG_SERVICE.log(ActivityLogRequest.builder()
                    .actorType(actor.getType())
                    .actorIdentifier(actor.getIdentifier())
                    .actorName(actor.getName())
                    .actionType(ActivityActionType.FACULTY_DELETED)
                    .entityType(ActivityEntityType.FACULTY)
                    .entityIdentifier(identifier)
                    .entityName(existing.getName())
                    .instituteIdentifier(existing.getInstituteIdentifier())
                    .description("Deleted faculty" + (existing.getName() != null ? " " + existing.getName() : ""))
                    .source("CONSOLE")
                    .build());
        }

        return new ResponseEntity<>("Faculty deleted successfully", HttpStatus.OK);
    }

    // ================ UPDATE FACULTY BY ID ===================== //
    @PutMapping("/{identifier}")
    public ResponseEntity<?> updateFacultyById(@PathVariable String identifier, @RequestBody Faculty faculty, HttpServletRequest request) {
        Faculty existing = this.FACULTY_SERVICE_IMPL.findById(identifier).orElse(null);
        if (existing == null) {
            return new ResponseEntity<>("Faculty not found", HttpStatus.NOT_FOUND);
        }
        faculty.setIdentifier(identifier);
        Faculty updated = this.FACULTY_SERVICE_IMPL.save(faculty);

        ResolvedActor actor = ACTOR_RESOLVER.resolve(request);
        if (actor.isAuthenticated()) {
            ACTIVITY_LOG_SERVICE.log(ActivityLogRequest.builder()
                    .actorType(actor.getType())
                    .actorIdentifier(actor.getIdentifier())
                    .actorName(actor.getName())
                    .actionType(ActivityActionType.FACULTY_UPDATED)
                    .entityType(ActivityEntityType.FACULTY)
                    .entityIdentifier(updated.getIdentifier())
                    .entityName(updated.getName())
                    .instituteIdentifier(updated.getInstituteIdentifier())
                    .description("Updated faculty" + (updated.getName() != null ? " " + updated.getName() : ""))
                    .metadata(Map.of("changedFields", ActivityLogChangeExtractor.extractChangedFields(existing, updated)))
                    .source("CONSOLE")
                    .build());
        }

        return new ResponseEntity<>(updated, HttpStatus.OK);
    }

    // ================ FIND BY INSTITUTE IDENTIFIER ===================== //
    @GetMapping("/institute/{instituteIdentifier}")
    public ResponseEntity<?> findByInstituteIdentifier(@PathVariable String instituteIdentifier) {
        return new ResponseEntity<>(this.FACULTY_SERVICE_IMPL.findByInstituteIdentifier(instituteIdentifier), HttpStatus.OK);
    }

    // ================ FIND BY EXPERIENCE YEARS GREATER THAN ===================== //
    @GetMapping("/experience-greater-than/{years}")
    public ResponseEntity<?> findByExperienceYearsGreaterThan(@PathVariable Integer years) {
        return new ResponseEntity<>(this.FACULTY_SERVICE_IMPL.findByExperienceYearsGreaterThan(years), HttpStatus.OK);
    }
}
