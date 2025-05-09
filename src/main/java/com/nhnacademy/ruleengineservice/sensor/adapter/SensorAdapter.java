package com.nhnacademy.ruleengineservice.sensor.adapter;

import com.nhnacademy.ruleengineservice.threshold.dto.ThresholdAnalysisDTO;

public interface SensorAdapter {
    ThresholdAnalysisDTO getAnalysisResult(String gatewayId);
}
