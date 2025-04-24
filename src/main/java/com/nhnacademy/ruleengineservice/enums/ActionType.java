package com.nhnacademy.ruleengineservice.enums;

public enum ActionType {
    SEND_ALERT("send_alert", "알림전송"),
    LOG_WARNING("log_warning","warning 로그발생"),
    SHUTDOWN_DEVICE("shutdown_device", "장치 강제 종료");

    private final String value;
    private final String desc;

    ActionType(String value, String desc) {
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
