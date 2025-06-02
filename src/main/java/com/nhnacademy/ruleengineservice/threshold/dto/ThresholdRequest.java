package com.nhnacademy.ruleengineservice.threshold.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ThresholdRequest {

    @JsonProperty("gateway_id")
    private Long gatewayId;

    @JsonProperty("status")
    private String status;
}
