package com.classes.Backend.Controller.activity;

import com.classes.Backend.Domain.activity.ActivityLog;
import com.classes.Backend.Service.activity.ActivityLogActorResolver;
import com.classes.Backend.Service.activity.ActivityLogService;
import com.classes.Backend.Service.activity.ResolvedActor;
import com.classes.Backend.dto.activity.ActivityLogFilterRequest;
import com.classes.Backend.dto.activity.ActivityLogPageResponse;
import com.classes.Backend.dto.activity.ActivityLogRequest;
import com.classes.Backend.dto.activity.ActivityLogStatsResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/activity-logs")
public class ActivityLogController {

    private final ActivityLogService activityLogService;
    private final ActivityLogActorResolver actorResolver;

    @GetMapping
    public ResponseEntity<ActivityLogPageResponse> search(ActivityLogFilterRequest request) {
        return ResponseEntity.ok(activityLogService.search(request));
    }

    @GetMapping("/{identifier}")
    public ResponseEntity<ActivityLog> getById(@PathVariable String identifier) {
        return ResponseEntity.ok(activityLogService.getById(identifier));
    }

    @GetMapping("/student/{userIdentifier}/timeline")
    public ResponseEntity<List<ActivityLog>> getStudentTimeline(
            @PathVariable String userIdentifier,
            @RequestParam(defaultValue = "100") int limit) {
        return ResponseEntity.ok(activityLogService.getStudentTimeline(userIdentifier, limit));
    }

    @GetMapping("/institute/{instituteIdentifier}/timeline")
    public ResponseEntity<List<ActivityLog>> getInstituteTimeline(
            @PathVariable String instituteIdentifier,
            @RequestParam(defaultValue = "100") int limit) {
        return ResponseEntity.ok(activityLogService.getInstituteTimeline(instituteIdentifier, limit));
    }

    @GetMapping("/stats")
    public ResponseEntity<ActivityLogStatsResponse> getStats() {
        return ResponseEntity.ok(activityLogService.getStats());
    }

    @PostMapping("/track")
    public ResponseEntity<Void> track(@RequestBody ActivityLogRequest request, HttpServletRequest httpRequest) {
        ResolvedActor actor = actorResolver.resolve(httpRequest);
        if (!actor.isAuthenticated()) {
            return ResponseEntity.noContent().build();
        }

        ActivityLogRequest enriched = ActivityLogRequest.builder()
                .actorType(actor.getType())
                .actorIdentifier(actor.getIdentifier())
                .actorName(actor.getName())
                .actionType(request.getActionType())
                .entityType(request.getEntityType())
                .entityIdentifier(request.getEntityIdentifier())
                .entityName(request.getEntityName())
                .instituteIdentifier(request.getInstituteIdentifier())
                .description(request.getDescription())
                .metadata(request.getMetadata())
                .ipAddress(httpRequest.getRemoteAddr())
                .userAgent(httpRequest.getHeader("User-Agent"))
                .source(request.getSource())
                .build();

        activityLogService.log(enriched);
        return ResponseEntity.accepted().build();
    }
}
