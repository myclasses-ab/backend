package com.classes.Backend.Service.activity;

import com.classes.Backend.Domain.activity.ActivityActorType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResolvedActor {
    private ActivityActorType type;
    private String identifier;
    private String name;
    private boolean authenticated;
}
