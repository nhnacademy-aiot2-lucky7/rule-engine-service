package com.nhnacademy.ruleengineservice.common.controller;

import com.nhnacademy.ruleengineservice.threshold.dto.ThresholdRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/webhook")
public class ThresholdController {

    @PostMapping("/threshold_complete")
    public ResponseEntity<Void> receiveThresholdResult(@RequestBody ThresholdRequest request) {

    }
}
