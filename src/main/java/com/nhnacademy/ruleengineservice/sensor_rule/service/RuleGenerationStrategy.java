package com.nhnacademy.ruleengineservice.sensor_rule.service;

import com.nhnacademy.ruleengineservice.event.producer.EventProducer;
import com.nhnacademy.ruleengineservice.threshold.dto.ThresholdAnalysisDTO;

public interface RuleGenerationStrategy {
    void generate(ThresholdAnalysisDTO dto, SensorRuleService sensorRuleService);
}
