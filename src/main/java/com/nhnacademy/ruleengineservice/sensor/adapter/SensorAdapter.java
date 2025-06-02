package com.nhnacademy.ruleengineservice.sensor.adapter;

import com.nhnacademy.ruleengineservice.threshold.dto.ThresholdAnalysisDTO;

import java.util.List;

public interface SensorAdapter {
    List<ThresholdAnalysisDTO> getAnalysisResult(Long gatewayId);
}
