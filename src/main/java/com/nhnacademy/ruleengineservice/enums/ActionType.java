package com.nhnacademy.ruleengineservice.enums;

public enum ActionType {
    SEND_ALERT("send_alert"),
    LOG_WARNING("log_warning"),
    SHUTDOWN_DEVICE("shutdown_device");

    private final String value;

    ActionType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
