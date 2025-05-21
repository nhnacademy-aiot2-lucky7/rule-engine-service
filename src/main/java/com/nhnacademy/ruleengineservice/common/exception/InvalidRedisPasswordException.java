package com.nhnacademy.ruleengineservice.common.exception;

public class InvalidRedisPasswordException extends CommonHttpException {

    private static final int HTTP_STATUS_CODE = 500;

    public InvalidRedisPasswordException() {
        super(HTTP_STATUS_CODE, "Redis 구성 요소 설정에 실패했습니다.");
    }

    public InvalidRedisPasswordException(final String message) {
        super(HTTP_STATUS_CODE, message);
    }
}
