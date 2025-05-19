package com.nhnacademy.ruleengineservice.common.exception;

public class BadRequestException extends CommonHttpException {

    private static final int HTTP_STATUS_CODE = 400;

    public BadRequestException(String message) {
        super(HTTP_STATUS_CODE, message);
    }
}
