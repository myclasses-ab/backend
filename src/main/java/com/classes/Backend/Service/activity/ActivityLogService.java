package com.classes.Backend.Service.activity;

import com.classes.Backend.Domain.activity.ActivityLog;
import com.classes.Backend.dto.activity.ActivityLogFilterRequest;
import com.classes.Backend.dto.activity.ActivityLogPageResponse;
import com.classes.Backend.dto.activity.ActivityLogRequest;
import com.classes.Backend.dto.activity.ActivityLogStatsResponse;

import java.util.List;

public interface ActivityLogService {
    void log(ActivityLogRequest request);
    ActivityLogPageResponse search(ActivityLogFilterRequest request);
    ActivityLog getById(String identifier);
    List<ActivityLog> getStudentTimeline(String userIdentifier, int limit);
    List<ActivityLog> getInstituteTimeline(String instituteIdentifier, int limit);
    ActivityLogStatsResponse getStats();
}
