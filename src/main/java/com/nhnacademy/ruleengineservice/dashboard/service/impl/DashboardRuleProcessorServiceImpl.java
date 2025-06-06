package com.nhnacademy.ruleengineservice.dashboard.service.impl;

import com.nhnacademy.ruleengineservice.common.exception.SensorRuleCreationException;
import com.nhnacademy.ruleengineservice.dashboard.dto.RuleCreateRequest;
import com.nhnacademy.ruleengineservice.dashboard.service.DashboardRuleProcessorService;
import com.nhnacademy.ruleengineservice.sensor_rule.service.SensorRuleGenerateService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class DashboardRuleProcessorServiceImpl implements DashboardRuleProcessorService {

    private final SensorRuleGenerateService sensorRuleGenerateService;

    @Override
    public void generateRulesFromCreateDto(RuleCreateRequest request) {
        try {
            sensorRuleGenerateService.generateRules(request);
            log.info("generateRulesFromCreateDto 통과");
        } catch (SensorRuleCreationException e) {
            log.error("룰 생성 실패.");
            throw e;
        }
    }
}
