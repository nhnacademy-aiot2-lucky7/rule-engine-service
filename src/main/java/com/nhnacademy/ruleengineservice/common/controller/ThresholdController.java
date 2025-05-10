package com.nhnacademy.ruleengineservice.common.controller;

import com.nhnacademy.ruleengineservice.threshold.dto.ThresholdRequest;
import com.nhnacademy.ruleengineservice.threshold.service.ThresholdRuleProcessorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/webhook")
public class ThresholdController {

    private final ThresholdRuleProcessorService thresholdRuleProcessorService;

    @PostMapping("/threshold_complete")
    public void receiveThresholdResult(@RequestBody ThresholdRequest request) {
        String gatewayId = request.getGatewayId();
        thresholdRuleProcessorService.generateRulesFromAnalysis(gatewayId);
    }
}
