package com.nhnacademy.ruleengineservice.sensor_rule.service;

import com.nhnacademy.ruleengineservice.dashboard.dto.RuleCreateRequest;
import com.nhnacademy.ruleengineservice.sensor_rule.domain.SensorRule;

@FunctionalInterface
public interface RuleGenerationStrategy {
    SensorRule generate(RuleCreateRequest createRequest);
}
