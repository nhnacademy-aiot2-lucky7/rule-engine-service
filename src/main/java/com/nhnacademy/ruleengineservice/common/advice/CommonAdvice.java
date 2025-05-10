package com.nhnacademy.ruleengineservice.common.advice;

import com.nhnacademy.ruleengineservice.common.exception.CommonHttpException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class CommonAdvice {

    @ExceptionHandler(CommonHttpException.class)
    public ResponseEntity<String> commonExceptionHandler(CommonHttpException e) {
        log.warn("CommonHttpException 발생: {}", e.getMessage());

        return ResponseEntity
                .status(e.getStatusCode())
                .body("CommonException: " + e.getMessage());
    }
}
