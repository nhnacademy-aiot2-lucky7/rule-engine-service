package com.nhnacademy.ruleengineservice.enums;

public enum DataType {
    TEMPERATURE("temperature", "온도"),
    HUMIDITY("humidity", "습도"),
    POWER("power", "전략");

    private final String value;
    private final String desc;

    DataType(String value, String desc) {
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
