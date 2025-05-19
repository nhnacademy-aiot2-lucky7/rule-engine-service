package com.nhnacademy.ruleengineservice.enums;

public enum SaveStatus {
    NEW("생성"),
    UPDATED("갱신");

    private final String desc;

    SaveStatus(String desc) {
        this.desc = desc;
    }

    public String getDesc() {
        return desc;
    }
}
