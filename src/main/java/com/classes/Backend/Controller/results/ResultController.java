package com.classes.Backend.Controller.results;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.classes.Backend.Domain.activity.ActivityActionType;
import com.classes.Backend.Domain.activity.ActivityEntityType;
import com.classes.Backend.Domain.results.Result;
import com.classes.Backend.Service.activity.ActivityLogActorResolver;
import com.classes.Backend.Service.activity.ActivityLogChangeExtractor;
import com.classes.Backend.Service.activity.ActivityLogService;
import com.classes.Backend.Service.activity.ResolvedActor;
import com.classes.Backend.Service.results.ResultServiceImpl;
import com.classes.Backend.dto.activity.ActivityLogRequest;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/results")
public class ResultController {

    private final ResultServiceImpl RESULT_SERVICE_IMPL;
    private final ActivityLogService ACTIVITY_LOG_SERVICE;
    private final ActivityLogActorResolver ACTOR_RESOLVER;

    // ================ CREATE RESULT ===================== //
    @PostMapping
    public ResponseEntity<?> saveResult(@RequestBody Result result, HttpServletRequest request) {
        Result saved = this.RESULT_SERVICE_IMPL.save(result);

        ResolvedActor actor = ACTOR_RESOLVER.resolve(request);
        if (actor.isAuthenticated()) {
            ACTIVITY_LOG_SERVICE.log(ActivityLogRequest.builder()
                    .actorType(actor.getType())
                    .actorIdentifier(actor.getIdentifier())
                    .actorName(actor.getName())
                    .actionType(ActivityActionType.RESULT_CREATED)
                    .entityType(ActivityEntityType.RESULT)
                    .entityIdentifier(saved.getIdentifier())
                    .entityName(saved.getStudentName())
                    .instituteIdentifier(saved.getInstituteIdentifier())
                    .description("Created result" + (saved.getExam() != null ? " for " + saved.getExam() : ""))
                    .source("CONSOLE")
                    .build());
        }

        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }

    // ================ CREATE ALL RESULTS ===================== //
    @PostMapping("/bulk")
    public ResponseEntity<?> saveAllResults(@RequestBody List<Result> results) {
        return new ResponseEntity<>(this.RESULT_SERVICE_IMPL.saveAll(results), HttpStatus.CREATED);
    }

    // ================ GET RESULT BY ID ===================== //
    @GetMapping("/{identifier}")
    public ResponseEntity<?> getResultById(@PathVariable String identifier) {
        return new ResponseEntity<>(this.RESULT_SERVICE_IMPL.findById(identifier), HttpStatus.OK);
    }

    // ================ GET ALL RESULTS ===================== //
    @GetMapping
    public ResponseEntity<?> getAllResults() {
        List<Result> allResults = this.RESULT_SERVICE_IMPL.findAll();
        return new ResponseEntity<>(allResults, HttpStatus.OK);
    }

    // ================ DELETE RESULT BY ID ===================== //
    @DeleteMapping("/{identifier}")
    public ResponseEntity<?> deleteResultById(@PathVariable String identifier, HttpServletRequest request) {
        Result existing = this.RESULT_SERVICE_IMPL.findById(identifier).orElse(null);

        this.RESULT_SERVICE_IMPL.deleteById(identifier);

        ResolvedActor actor = ACTOR_RESOLVER.resolve(request);
        if (actor.isAuthenticated() && existing != null) {
            ACTIVITY_LOG_SERVICE.log(ActivityLogRequest.builder()
                    .actorType(actor.getType())
                    .actorIdentifier(actor.getIdentifier())
                    .actorName(actor.getName())
                    .actionType(ActivityActionType.RESULT_DELETED)
                    .entityType(ActivityEntityType.RESULT)
                    .entityIdentifier(identifier)
                    .entityName(existing.getStudentName())
                    .instituteIdentifier(existing.getInstituteIdentifier())
                    .description("Deleted result" + (existing.getExam() != null ? " for " + existing.getExam() : ""))
                    .source("CONSOLE")
                    .build());
        }

        return new ResponseEntity<>("Result deleted successfully", HttpStatus.OK);
    }

    // ================ UPDATE RESULT BY ID ===================== //
    @PutMapping("/{identifier}")
    public ResponseEntity<?> updateResultById(@PathVariable String identifier, @RequestBody Result result, HttpServletRequest request) {
        Result existing = this.RESULT_SERVICE_IMPL.findById(identifier).orElse(null);
        if (existing == null) {
            return new ResponseEntity<>("Result not found", HttpStatus.NOT_FOUND);
        }
        result.setIdentifier(identifier);
        Result updated = this.RESULT_SERVICE_IMPL.save(result);

        ResolvedActor actor = ACTOR_RESOLVER.resolve(request);
        if (actor.isAuthenticated()) {
            ACTIVITY_LOG_SERVICE.log(ActivityLogRequest.builder()
                    .actorType(actor.getType())
                    .actorIdentifier(actor.getIdentifier())
                    .actorName(actor.getName())
                    .actionType(ActivityActionType.RESULT_UPDATED)
                    .entityType(ActivityEntityType.RESULT)
                    .entityIdentifier(updated.getIdentifier())
                    .entityName(updated.getStudentName())
                    .instituteIdentifier(updated.getInstituteIdentifier())
                    .description("Updated result" + (updated.getExam() != null ? " for " + updated.getExam() : ""))
                    .metadata(Map.of("changedFields", ActivityLogChangeExtractor.extractChangedFields(existing, updated)))
                    .source("CONSOLE")
                    .build());
        }

        return new ResponseEntity<>(updated, HttpStatus.OK);
    }

    // ================ FIND BY INSTITUTE IDENTIFIER ===================== //
    @GetMapping("/institute/{instituteIdentifier}")
    public ResponseEntity<?> findByInstituteIdentifier(@PathVariable String instituteIdentifier) {
        return new ResponseEntity<>(this.RESULT_SERVICE_IMPL.findByInstituteIdentifier(instituteIdentifier), HttpStatus.OK);
    }

    // ================ FIND FEATURED RESULTS ===================== //
    @GetMapping("/featured")
    public ResponseEntity<?> findByIsFeaturedTrue() {
        return new ResponseEntity<>(this.RESULT_SERVICE_IMPL.findByIsFeaturedTrue(), HttpStatus.OK);
    }
}
