package com.nhnacademy.ruleengineservice.threshold.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Value;

@Value
@AllArgsConstructor
public class ThresholdAnalysisDTO {

    @JsonProperty("gateway_id")
    Long gatewayId;

    @JsonProperty("sensor_id")
    String sensorId;

    @JsonProperty("type_en_name")
    String dataTypeEnName;

    @JsonProperty("type_kr_name")
    String dataTypeKrName;

    @JsonProperty("threshold_min")
    Double thresholdMin;

    @JsonProperty("threshold_max")
    Double thresholdMax;

    @JsonProperty("threshold_avg")
    Double thresholdAvg;

    @JsonProperty("avg_range_min")
    Double thresholdAvgMin;

    @JsonProperty("avg_range_max")
    Double thresholdAvgMax;
}
