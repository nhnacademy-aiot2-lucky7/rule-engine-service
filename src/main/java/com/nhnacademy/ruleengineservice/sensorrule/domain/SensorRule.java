package com.nhnacademy.ruleengineservice.sensorrule.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

/**
 * {@code SensorRule} 클래스는 특정 게이트웨이와 센서에 대한 룰들을 관리하는 클래스입니다.
 * 각 센서에 대해 여러 룰을 등록하고, 해당 룰에 맞춰 센서 데이터의 위반 여부를 판단할 수 있습니다.
 */
@Data
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SensorRule {
    /**
     * 센서와 게이트웨이를 식별하기 위한 유니크한 ID (형식: "gatewayId_sensorId_type")
     */
    private String gatewayIdAndSensorIdAndType;

    /**
     * 센서에 적용된 룰 리스트
     */
    private List<Rule> rules = new ArrayList<>();

    /**
     * {@code SensorRule} 객체를 생성하는 생성자입니다.
     *
     * @param gatewayIdAndSensorIdAndType 센서와 게이트웨이를 식별하는 ID
     * @param rule 해당 센서에 적용될 룰
     */
    private SensorRule(String gatewayIdAndSensorIdAndType, Rule rule) {
        this.gatewayIdAndSensorIdAndType = gatewayIdAndSensorIdAndType;
        this.rules.add(rule);
    }

    /**
     * 새로운 {@code SensorRule} 객체를 생성하는 팩토리 메소드입니다.
     *
     * @param gatewayIdAndSensorIdAndType 센서와 게이트웨이를 식별하는 ID
     * @param rule 해당 센서에 적용될 룰
     * @return 새로운 {@code SensorRule} 객체
     */
    public static SensorRule ofNewRule(String gatewayIdAndSensorIdAndType, Rule rule) {
        return new SensorRule(gatewayIdAndSensorIdAndType, rule);
    }

    /**
     * 기존 룰 리스트에 새로운 룰을 추가합니다.
     *
     * @param rule 추가할 룰
     */
    public void addRule(Rule rule) {
        this.rules.add(rule);
    }

    /**
     * 여러 개의 룰을 한 번에 추가할 수 있는 메소드입니다.
     *
     * @param rules 추가할 룰들의 리스트
     */
    public void addRules(List<Rule> rules) {
        this.rules.addAll(rules);
    }
}