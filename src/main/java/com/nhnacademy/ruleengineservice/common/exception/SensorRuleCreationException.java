package com.nhnacademy.ruleengineservice.common.exception;

public class SensorRuleCreationException extends CommonHttpException {

    private static final int HTTP_STATUS_CODE = 500;

    public SensorRuleCreationException() {
        super(HTTP_STATUS_CODE, "룰 생성에 실패했습니다.");
    }

    public SensorRuleCreationException(final String message) {
        super(HTTP_STATUS_CODE, message);
    }
}
