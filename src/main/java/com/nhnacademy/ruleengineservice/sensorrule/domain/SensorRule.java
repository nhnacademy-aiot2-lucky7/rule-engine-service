package com.nhnacademy.ruleengineservice.sensorrule.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.util.ArrayList;
import java.util.List;


@Data
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SensorRule {
    private String deviceIdAndType;
    private List<Rule> rules = new ArrayList<>();

    private SensorRule(String deviceIdAndType, Rule rule) {
        this.deviceIdAndType = deviceIdAndType;
        this.rules.add(rule);
    }

    public static SensorRule ofNewRule(String deviceIdAndType, Rule rule) {
        return new SensorRule(deviceIdAndType, rule);
    }

    // 여러 룰을 추가할 수 있는 메소드
    public void addRule(Rule rule) {
        this.rules.add(rule);
    }

    // 여러 룰을 추가할 수 있는 메소드 (다수의 룰을 한 번에 추가)
    public void addRules(List<Rule> rules) {
        this.rules.addAll(rules);
    }
}
