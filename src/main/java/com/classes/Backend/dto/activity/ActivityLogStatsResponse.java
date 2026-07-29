package com.classes.Backend.dto.activity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ActivityLogStatsResponse {

    private long totalToday;
    private long totalWeek;
    private long totalMonth;
    private List<TopActor> topStudents;
    private List<TopActor> topInstitutes;
    private List<ActionCount> actionCounts;
}
