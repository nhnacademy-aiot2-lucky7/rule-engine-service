package com.nhnacademy.ruleengineservice.threshold.service.impl;

import com.nhnacademy.ruleengineservice.sensor.adapter.SensorAdapter;
import com.nhnacademy.ruleengineservice.threshold.dto.ThresholdAnalysisDTO;
import com.nhnacademy.ruleengineservice.threshold.service.ThresholdRuleProcessorService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ThresholdRuleProcessorServiceImpl implements ThresholdRuleProcessorService {

    private final SensorAdapter sensorAdapter;

    public void generateRulesFromAnalysis(String gatewayId) {
        ThresholdAnalysisDTO analysisDTO = sensorAdapter.getAnalysisResult(gatewayId);

    }
}
