package com.classes.Backend.dto.activity;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class InstituteActivitySummary {
    private String identifier;
    private String name;
    private Long eventCount;
    private LocalDateTime lastActiveAt;
}
