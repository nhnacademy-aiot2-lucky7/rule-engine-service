package com.nhnacademy.ruleengineservice.sensor_rule.service;

import com.nhnacademy.ruleengineservice.sensor_rule.domain.SensorRule;
import com.nhnacademy.ruleengineservice.threshold.dto.ThresholdAnalysisDTO;

@FunctionalInterface
public interface RuleGenerationStrategy {
    SensorRule generate(ThresholdAnalysisDTO dto);
}
