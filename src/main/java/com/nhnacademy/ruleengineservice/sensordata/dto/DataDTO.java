package com.nhnacademy.ruleengineservice.sensordata.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Value;

@Value
@AllArgsConstructor
public class DataDTO {

    @JsonProperty("gateway_id")
    String gatewayId;

    @JsonProperty("sensor_id")
    String sensorId;

    @JsonProperty("type")
    String dataType;

    @JsonProperty("value")
    Double value;

    @JsonProperty("timestamp")
    Long timestamp;
}
