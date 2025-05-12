package com.nhnacademy.ruleengineservice.threshold.dto;

import lombok.AllArgsConstructor;
import lombok.Value;

@Value
@AllArgsConstructor
public class ThresholdAnalysisDTO {

    String gatewayId;

    String sensorId;

    String dataType;

    Double thresholdMin;

    Double thresholdMax;

    Double thresholdAvg;

    Double thresholdAvgMin;

    Double thresholdAvgMax;
}
