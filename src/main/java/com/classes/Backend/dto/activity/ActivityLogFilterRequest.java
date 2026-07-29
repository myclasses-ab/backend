package com.classes.Backend.dto.activity;

import com.classes.Backend.Domain.activity.ActivityActionType;
import com.classes.Backend.Domain.activity.ActivityActorType;
import com.classes.Backend.Domain.activity.ActivityEntityType;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ActivityLogFilterRequest {
    private ActivityActorType actorType;
    private ActivityActionType actionType;
    private ActivityEntityType entityType;
    private String actorIdentifier;
    private String instituteIdentifier;
    private LocalDateTime fromDate;
    private LocalDateTime toDate;
    private String search;
    private int page = 0;
    private int size = 25;
    private String sortBy = "createdAt";
    private String sortDirection = "desc";
}
