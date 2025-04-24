package com.nhnacademy.ruleengineservice.enums;

public enum Operator {
    GREATER_THAN(">", "초과하면"),
    LESS_THAN("<", "미만이면"),
    GREATER_THAN_OR_EQUAL(">=", "이상이면"),
    LESS_THAN_OR_EQUAL("<=", "이하면"),
    EQUAL("=", "이면");

    private final String value;
    private final String desc;

    Operator(String value, String desc) {
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
