package com.nhnacademy.ruleengineservice.sensor_rule.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.nhnacademy.ruleengineservice.enums.ActionType;
import com.nhnacademy.ruleengineservice.enums.Operator;
import com.nhnacademy.ruleengineservice.enums.RuleType;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@EqualsAndHashCode
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SensorRule {

    // 센서 식별 정보
    private String gatewayId;
    private String sensorId;
    private String dataType; // 예: 온도, 습도 등

    // 룰 정의
    private RuleType ruleType; // 예: max, min, avg 등
    private Operator operator; // 예: 초과, 미만 등
    private Double value; // 기준 값
    private Double range; // 범위 값 (선택적)
    private ActionType action; // 룰 위반 시 수행할 액션

    // Static 팩토리 메소드
    public static SensorRule createRule(String gatewayId, String sensorId, String dataType,
                                  RuleType ruleType, Operator operator,
                                  Double value, Double range, ActionType action) {
        return SensorRule.builder()
                .gatewayId(gatewayId)
                .sensorId(sensorId)
                .dataType(dataType)
                .ruleType(ruleType)
                .operator(operator)
                .value(value)
                .range(range)
                .action(action)
                .build();
    }

    // 센서 룰의 Redis 키를 생성하는 메소드
    public String getRedisKey() {
        return String.format("rule:gateway:%s:sensor:%s:%s:%s", gatewayId, sensorId, dataType, ruleType);
    }
}
