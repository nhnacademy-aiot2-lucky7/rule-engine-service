package com.nhnacademy.ruleengineservice.dashboard.service.impl;

import com.nhnacademy.ruleengineservice.common.exception.SensorRuleException;
import com.nhnacademy.ruleengineservice.dashboard.dto.RuleCreateRequest;
import com.nhnacademy.ruleengineservice.dashboard.dto.RuleDeleteRequest;
import com.nhnacademy.ruleengineservice.dashboard.service.DashboardRuleProcessorService;
import com.nhnacademy.ruleengineservice.sensor_rule.service.SensorRuleDeleteService;
import com.nhnacademy.ruleengineservice.sensor_rule.service.SensorRuleGenerateService;
import com.nhnacademy.ruleengineservice.sensor_rule.service.SensorRuleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class DashboardRuleProcessorServiceImpl implements DashboardRuleProcessorService {

    private final SensorRuleGenerateService sensorRuleGenerateService;
    private final SensorRuleDeleteService sensorRuleDeleteService;

    @Override
    public void generateRulesFromCreateDto(RuleCreateRequest request) {
        try {
            sensorRuleGenerateService.generateRules(request);
        } catch (SensorRuleException e) {
            log.error("룰 생성 실패.");
            throw e;
        }
    }

    @Override
    public void deleteRulesFromDeleteDto(RuleDeleteRequest request) {
        try {
            sensorRuleDeleteService.deleteRules(request);
        } catch (SensorRuleException e) {
            log.error("룰 삭제 실패.");
            throw e;
        }
    }
}
