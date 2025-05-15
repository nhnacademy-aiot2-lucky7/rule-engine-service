package com.nhnacademy.ruleengineservice.sensor_rule.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.nhnacademy.ruleengineservice.enums.ActionType;
import com.nhnacademy.ruleengineservice.enums.Operator;
import com.nhnacademy.ruleengineservice.enums.RuleType;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.jetbrains.annotations.NotNull;

@Data
@Builder
@EqualsAndHashCode
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SensorRule {

    // 필수값
    @NotNull
    private String gatewayId;

    @NotNull
    private String sensorId;

    @NotNull
    private String dataType; // 예: 온도, 습도 등

    @NotNull
    private RuleType ruleType; // 예: max, min, avg 등

    @NotNull
    private Operator operator; // 예: 초과, 미만 등

    @NotNull
    private ActionType action; // 룰 위반 시 수행할 액션

    @NotNull
    private Double value; // 기준 값

    private Double minValue; // 기준 값의 최소값 (선택적)
    
    private Double maxValue; // 기준 값의 최소값 (선택적)


    // 센서 룰의 Redis 키를 생성하는 메소드
    public String getRedisKey() {
        return String.format("rule:gateway:%s:sensor:%s:%s:%s", gatewayId, sensorId, dataType, ruleType);
    }

}
