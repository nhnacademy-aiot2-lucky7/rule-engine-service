package com.nhnacademy.ruleengineservice.common.exception;

public class MissingRequiredSensorRuleFieldException extends RuntimeException {
    public MissingRequiredSensorRuleFieldException(String message) {
        super(message);
    }
}
