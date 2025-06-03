package com.nhnacademy.ruleengineservice.sensor_rule.service;

import com.nhnacademy.ruleengineservice.dashboard.dto.RuleCreateRequest;

public interface SensorRuleGenerateService {
    void generateRules(RuleCreateRequest createRequest);
}
