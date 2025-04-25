package com.nhnacademy.ruleengineservice.sensorrule.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.nhnacademy.ruleengineservice.enums.ActionType;
import com.nhnacademy.ruleengineservice.enums.Operator;
import com.nhnacademy.ruleengineservice.enums.RuleType;
import com.nhnacademy.ruleengineservice.enums.SensorType;
import lombok.Builder;
import lombok.Data;

import java.util.List;


@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SensorRule {
    private String sensorId;              // 센서 ID (예: sensor:1001)
    private SensorType sensorType;       // 센서 타입 (예: TEMPERATURE)
    private List<Rule> rules;

    @Data
    @Builder
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Rule {
        private RuleType type;
        private Operator operator;
        private Double value;
        private String range;
        private ActionType action;
    }
}