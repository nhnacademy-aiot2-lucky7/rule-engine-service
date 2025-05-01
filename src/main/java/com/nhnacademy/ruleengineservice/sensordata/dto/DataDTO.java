package com.nhnacademy.ruleengineservice.sensordata.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Value;

@Value
public class DataDTO {

    String gatewayId;

    String sensorId;

    String dataType;

    double value;

    long timestamp;


    @JsonCreator
    public DataDTO(
            @JsonProperty("gateway-id") String gatewayId,
            @JsonProperty("sensor-id") String sensorId,
            @JsonProperty("type") String dataType,
            @JsonProperty("value") double value,
            @JsonProperty("timestamp") long timestamp
    ) {
        this.gatewayId = gatewayId;
        this.sensorId = sensorId;
        this.dataType = dataType;
        this.value = value;
        this.timestamp = timestamp;
    }
}
