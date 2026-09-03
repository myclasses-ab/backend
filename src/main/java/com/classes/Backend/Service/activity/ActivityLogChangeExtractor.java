package com.classes.Backend.Service.activity;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Slf4j
public final class ActivityLogChangeExtractor {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper().registerModule(new JavaTimeModule());
    private static final Set<String> IGNORED_FIELDS = Set.of(
            "identifier", "createdAt", "updatedAt", "createdBy",
            "facilities", "matchingCourses"
    );

    private ActivityLogChangeExtractor() {
    }

    public static Map<String, Object> extractChangedFields(Object oldObj, Object newObj) {
        if (oldObj == null || newObj == null) {
            return Map.of();
        }
        try {
            Map<String, Object> oldMap = convertToMap(oldObj);
            Map<String, Object> newMap = convertToMap(newObj);

            Map<String, Object> changed = new HashMap<>();
            for (String key : newMap.keySet()) {
                if (IGNORED_FIELDS.contains(key)) {
                    continue;
                }
                Object oldValue = oldMap.get(key);
                Object newValue = newMap.get(key);
                if (!areEqual(oldValue, newValue)) {
                    Map<String, Object> diff = new HashMap<>();
                    diff.put("old", oldValue);
                    diff.put("new", newValue);
                    changed.put(key, diff);
                }
            }
            return changed;
        } catch (Exception e) {
            log.warn("Failed to extract changed fields for activity log", e);
            return Map.of();
        }
    }

    @SuppressWarnings("unchecked")
    private static Map<String, Object> convertToMap(Object obj) {
        if (obj instanceof Map) {
            return (Map<String, Object>) obj;
        }
        return OBJECT_MAPPER.convertValue(obj, Map.class);
    }

    private static boolean areEqual(Object a, Object b) {
        if (a == null && b == null) {
            return true;
        }
        if (a == null || b == null) {
            return false;
        }
        return a.equals(b);
    }
}
