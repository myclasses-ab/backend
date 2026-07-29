package com.classes.Backend.Service.activity;

import com.classes.Backend.Domain.activity.ActivityActionType;
import com.classes.Backend.Domain.activity.ActivityActorType;
import com.classes.Backend.Domain.activity.ActivityEntityType;
import com.classes.Backend.Domain.activity.ActivityLog;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ActivityLogSpecification {

    public static Specification<ActivityLog> withFilter(
            ActivityActorType actorType,
            ActivityActionType actionType,
            ActivityEntityType entityType,
            String actorIdentifier,
            String instituteIdentifier,
            LocalDateTime fromDate,
            LocalDateTime toDate,
            String searchPattern
    ) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (actorType != null) {
                predicates.add(cb.equal(root.get("actorType"), actorType));
            }
            if (actionType != null) {
                predicates.add(cb.equal(root.get("actionType"), actionType));
            }
            if (entityType != null) {
                predicates.add(cb.equal(root.get("entityType"), entityType));
            }
            if (actorIdentifier != null && !actorIdentifier.isBlank()) {
                predicates.add(cb.equal(root.get("actorIdentifier"), actorIdentifier));
            }
            if (instituteIdentifier != null && !instituteIdentifier.isBlank()) {
                predicates.add(cb.equal(root.get("instituteIdentifier"), instituteIdentifier));
            }
            if (fromDate != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("createdAt"), fromDate));
            }
            if (toDate != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("createdAt"), toDate));
            }
            if (searchPattern != null && !searchPattern.isBlank()) {
                predicates.add(cb.or(
                        cb.like(cb.lower(root.get("entityName").as(String.class)), searchPattern),
                        cb.like(cb.lower(root.get("description").as(String.class)), searchPattern),
                        cb.like(cb.lower(root.get("actorName").as(String.class)), searchPattern)
                ));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
