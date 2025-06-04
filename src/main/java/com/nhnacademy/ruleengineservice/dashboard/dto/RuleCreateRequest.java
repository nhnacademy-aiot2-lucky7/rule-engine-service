package com.nhnacademy.ruleengineservice.dashboard.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class RuleCreateRequest {

    @JsonProperty("gateway_id")
    Long gatewayId;

    @JsonProperty("sensor_id")
    String sensorId;

    @JsonProperty("department_id")
    String departmentId;

    @JsonProperty("type_en_name")
    String dataTypeEnName;

    @JsonProperty("type_kr_name")
    String dataTypeKrName;

    @JsonProperty("threshold_min")
    Double thresholdMin;

    @JsonProperty("threshold_max")
    Double thresholdMax;
}