package com.classes.Backend.Domain.enums;

public enum Standard {
    STANDARD_10("10"),
    STANDARD_11("11"),
    STANDARD_12("12"),
    DROPPER("DROPPER"),
    STANDARD_11_AND_12("11_AND_12"),
    GRADUATE("GRADUATE"),
    OTHER("OTHER");

    private final String value;

    Standard(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
