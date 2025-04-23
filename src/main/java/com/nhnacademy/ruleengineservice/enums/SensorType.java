package com.nhnacademy.ruleengineservice.enums;

public enum SensorType {
    TEMPERATURE("temperature"),
    HUMIDITY("humidity"),
    POWER("power");

    private final String value;

    SensorType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
