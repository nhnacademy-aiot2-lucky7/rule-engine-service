package com.nhnacademy.ruleengineservice.common.advice;

import com.nhnacademy.ruleengineservice.common.exception.CommonHttpException;
import com.nhnacademy.ruleengineservice.common.exception.SensorRuleCreationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.io.PrintWriter;
import java.io.StringWriter;

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

    @ExceptionHandler(Throwable.class)
    public ResponseEntity<String> exceptionHandler(Throwable e){
        log.warn("Throwable 발생: {}", e.getMessage());

        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        e.printStackTrace(pw);
        String stackTrace = sw.toString();

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Internal Server Error 발생\n" + stackTrace);
    }

    @ExceptionHandler(SensorRuleCreationException.class)
    public ResponseEntity<String> handleSensorRuleCreationException(SensorRuleCreationException e) {
        log.warn("SensorRule 필수값 누락 예외 발생: {}", e.getMessage());

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body("SensorRule 생성 실패: " + e.getMessage());
    }

}
