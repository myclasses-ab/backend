package com.classes.Backend.dto.activity;

import com.classes.Backend.Domain.activity.ActivityActionType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ActionCount {

    private ActivityActionType actionType;
    private long count;
}
