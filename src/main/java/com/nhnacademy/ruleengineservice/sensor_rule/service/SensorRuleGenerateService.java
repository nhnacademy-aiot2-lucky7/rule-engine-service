package com.nhnacademy.ruleengineservice.sensor_rule.service;

import com.nhnacademy.ruleengineservice.threshold.dto.ThresholdAnalysisDTO;

import java.util.List;

public interface SensorRuleGenerateService {
    void generateRules(List<ThresholdAnalysisDTO> analysisDTOList);
}
