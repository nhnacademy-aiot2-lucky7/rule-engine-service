package com.nhnacademy.ruleengineservice.sensor_rule.service;

import com.nhnacademy.ruleengineservice.dashboard.dto.RuleDeleteRequest;

public interface SensorRuleDeleteService {
    void deleteRules(RuleDeleteRequest request);
}
