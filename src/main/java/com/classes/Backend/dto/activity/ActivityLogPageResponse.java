package com.classes.Backend.dto.activity;

import com.classes.Backend.Domain.activity.ActivityLog;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ActivityLogPageResponse {
    private List<ActivityLog> content;
    private int page;
    private int size;
    private long totalElements;
    private int totalPages;
}
