package com.nhnacademy.ruleengineservice.common.exception;

public class SensorRuleException extends CommonHttpException {

    private static final int HTTP_STATUS_CODE = 500;

    public SensorRuleException() {
        super(HTTP_STATUS_CODE, "룰 생성 또는 삭제에 실패했습니다.");
    }

    public SensorRuleException(final String message) {
        super(HTTP_STATUS_CODE, message);
    }
}
