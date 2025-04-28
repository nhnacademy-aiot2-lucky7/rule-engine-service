package com.nhnacademy.ruleengineservice.sensorrule.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Value;

@Value
public class DataDTO {

    String deviceId;

    String dataType;

    String location;

    Object value;

    long time;


    @JsonCreator
    public DataDTO(
            @JsonProperty("deviceId") String deviceId,
            @JsonProperty("dataType") String dataType,
            @JsonProperty("location") String location,
            @JsonProperty("value") Object value,
            @JsonProperty("time") long time
    ) {
        this.deviceId = deviceId;
        this.dataType = dataType;
        this.location = location;
        this.value = value;
        this.time = time;
    }
}
