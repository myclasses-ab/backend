package com.classes.Backend.Service.activity;

import com.classes.Backend.Domain.activity.ActivityActionType;
import com.classes.Backend.Domain.activity.ActivityActorType;
import com.classes.Backend.Domain.activity.ActivityLog;
import com.classes.Backend.Repository.activity.ActivityLogRepository;
import com.classes.Backend.dto.activity.ActionCount;
import com.classes.Backend.dto.activity.ActivityLogFilterRequest;
import com.classes.Backend.dto.activity.ActivityLogPageResponse;
import com.classes.Backend.dto.activity.ActivityLogRequest;
import com.classes.Backend.dto.activity.ActivityLogStatsResponse;
import com.classes.Backend.dto.activity.TopActor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ActivityLogServiceImpl implements ActivityLogService {

    private final ActivityLogRepository activityLogRepository;
    private final ObjectMapper objectMapper;

    @Async("activityLogTaskExecutor")
    @Override
    public void log(ActivityLogRequest request) {
        try {
            ActivityLog activityLog = ActivityLog.builder()
                    .actorType(request.getActorType())
                    .actorIdentifier(request.getActorIdentifier())
                    .actorName(request.getActorName())
                    .actionType(request.getActionType())
                    .entityType(request.getEntityType())
                    .entityIdentifier(request.getEntityIdentifier())
                    .entityName(request.getEntityName())
                    .instituteIdentifier(request.getInstituteIdentifier())
                    .description(request.getDescription())
                    .oldValue(toJson(request.getOldValue()))
                    .newValue(toJson(request.getNewValue()))
                    .metadata(toJson(request.getMetadata()))
                    .ipAddress(request.getIpAddress())
                    .userAgent(request.getUserAgent())
                    .source(request.getSource())
                    .build();
            activityLogRepository.save(activityLog);
        } catch (Exception e) {
            log.error("Failed to persist activity log", e);
        }
    }

    @Override
    public ActivityLogPageResponse search(ActivityLogFilterRequest request) {
        Sort.Direction direction = "asc".equalsIgnoreCase(request.getSortDirection())
                ? Sort.Direction.ASC
                : Sort.Direction.DESC;
        Sort sort = Sort.by(direction, request.getSortBy());
        Pageable pageable = PageRequest.of(request.getPage(), request.getSize(), sort);

        String searchPattern = (request.getSearch() == null || request.getSearch().isBlank())
                ? null
                : "%" + request.getSearch().toLowerCase() + "%";

        Page<ActivityLog> page = activityLogRepository.findAll(
                ActivityLogSpecification.withFilter(
                        request.getActorType(),
                        request.getActionType(),
                        request.getEntityType(),
                        request.getActorIdentifier(),
                        request.getInstituteIdentifier(),
                        request.getFromDate(),
                        request.getToDate(),
                        searchPattern
                ),
                pageable
        );

        return ActivityLogPageResponse.builder()
                .content(page.getContent())
                .page(page.getNumber())
                .size(page.getSize())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .build();
    }

    @Override
    public ActivityLog getById(String identifier) {
        return activityLogRepository.findById(identifier)
                .orElseThrow(() -> new RuntimeException("Activity log not found"));
    }

    @Override
    public List<ActivityLog> getStudentTimeline(String userIdentifier, int limit) {
        Pageable pageable = PageRequest.of(0, limit, Sort.by(Sort.Direction.DESC, "createdAt"));
        return activityLogRepository.findByActorTypeAndActorIdentifier(ActivityActorType.STUDENT, userIdentifier, pageable)
                .getContent();
    }

    @Override
    public List<ActivityLog> getInstituteTimeline(String instituteIdentifier, int limit) {
        Pageable pageable = PageRequest.of(0, limit, Sort.by(Sort.Direction.DESC, "createdAt"));
        return activityLogRepository.findByInstituteIdentifier(instituteIdentifier, pageable)
                .getContent();
    }

    @Override
    public ActivityLogStatsResponse getStats() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime today = now.toLocalDate().atStartOfDay();
        LocalDateTime weekAgo = today.minusDays(7);
        LocalDateTime monthAgo = today.minusDays(30);

        List<TopActor> topStudents = toTopActors(
                activityLogRepository.findTopActorsByTypeSince(ActivityActorType.STUDENT, weekAgo, PageRequest.of(0, 5))
        );

        List<TopActor> topInstitutes = toTopActors(
                activityLogRepository.findTopActorsByTypeSince(ActivityActorType.INSTITUTE_ADMIN, weekAgo, PageRequest.of(0, 5))
        );

        List<ActionCount> actionCounts = toActionCounts(
                activityLogRepository.countByActionTypeSince(weekAgo)
        );

        return ActivityLogStatsResponse.builder()
                .totalToday(activityLogRepository.countSince(today))
                .totalWeek(activityLogRepository.countSince(weekAgo))
                .totalMonth(activityLogRepository.countSince(monthAgo))
                .topStudents(topStudents)
                .topInstitutes(topInstitutes)
                .actionCounts(actionCounts)
                .build();
    }

    private List<TopActor> toTopActors(List<Object[]> rows) {
        return rows.stream()
                .map(row -> TopActor.builder()
                        .identifier((String) row[0])
                        .name((String) row[1])
                        .count((Long) row[2])
                        .build())
                .collect(Collectors.toList());
    }

    private List<ActionCount> toActionCounts(List<Object[]> rows) {
        return rows.stream()
                .map(row -> ActionCount.builder()
                        .actionType((ActivityActionType) row[0])
                        .count((Long) row[1])
                        .build())
                .collect(Collectors.toList());
    }

    private String toJson(Object value) {
        if (value == null) {
            return null;
        }
        try {
            return objectMapper.writeValueAsString(value);
        } catch (JsonProcessingException e) {
            log.warn("Failed to serialize activity log value", e);
            return "{\"error\":\"serialization_failed\"}";
        }
    }
}
