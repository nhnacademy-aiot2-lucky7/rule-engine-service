package com.nhnacademy.ruleengineservice.common.exception;

public class SensorRuleNotFoundException extends RuntimeException {
    public SensorRuleNotFoundException(String deviceId, String dataType) {
        super("No rules found for deviceId: " + deviceId + " and dataType: " + dataType);
    }
}
