package com.nhnacademy.ruleengineservice.threshold.service;

import com.nhnacademy.ruleengineservice.threshold.dto.ThresholdRequest;

public interface ThresholdRuleProcessorService {
    void generateRulesFromAnalysis(ThresholdRequest request);
}
