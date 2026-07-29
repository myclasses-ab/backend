package com.classes.Backend.dto.activity;

import com.classes.Backend.Domain.activity.ActivityActionType;
import com.classes.Backend.Domain.activity.ActivityActorType;
import com.classes.Backend.Domain.activity.ActivityEntityType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ActivityLogRequest {
    private ActivityActorType actorType;
    private String actorIdentifier;
    private String actorName;
    private ActivityActionType actionType;
    private ActivityEntityType entityType;
    private String entityIdentifier;
    private String entityName;
    private String instituteIdentifier;
    private String description;
    private Object oldValue;
    private Object newValue;
    private Map<String, Object> metadata;
    private String ipAddress;
    private String userAgent;
    private String source;
}
