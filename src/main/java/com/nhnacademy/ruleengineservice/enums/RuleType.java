package com.nhnacademy.ruleengineservice.enums;

public enum RuleType {
    THRESHOLD("threshold"),
    AVG("avg"),
    MIN("min"),
    MAX("max");

    private final String value;

    RuleType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
