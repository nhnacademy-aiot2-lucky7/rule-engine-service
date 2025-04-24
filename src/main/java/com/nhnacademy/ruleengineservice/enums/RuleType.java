package com.nhnacademy.ruleengineservice.enums;

public enum RuleType {
    THRESHOLD("threshold", "임계값이"),
    AVG("avg", "평균값이"),
    MIN("min","최소값이"),
    MAX("max","최댓값이");

    private final String value;
    private final String desc;

    RuleType(String value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public String getValue() {
        return value;
    }

    public String getDesc() {
        return desc;
    }
}
