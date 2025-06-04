package com.nhnacademy.ruleengineservice.sensor_rule.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.nhnacademy.ruleengineservice.enums.ActionType;
import com.nhnacademy.ruleengineservice.enums.Operator;
import com.nhnacademy.ruleengineservice.enums.RuleType;
import lombok.*;
import org.jetbrains.annotations.NotNull;

@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
@EqualsAndHashCode
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class SensorRule {

    // 필수값
    @NotNull
    private Long gatewayId;

    @NotNull
    private String sensorId;

    @NotNull
    private String departmentId;

    @NotNull
    private String dataTypeEnName; // 예: 온도, 습도 등

    @NotNull
    private String dataTypeKrName;

    @NotNull
    private RuleType ruleType; // 예: max, min, avg 등

    @NotNull
    private Operator operator; // 예: 초과, 미만 등

    @NotNull
    private ActionType action; // 룰 위반 시 수행할 액션

    @NotNull
    private Double value; // 기준 값

    private Double minValue; // 기준 값의 최소값 (선택적)

    private Double maxValue; // 기준 값의 최대값 (선택적)

    // 센서 룰의 Redis 키를 생성하는 메소드
    public String getRedisKey() {
        return String.format("rule:gateway:%s:sensor:%s:%s:%s", gatewayId, sensorId, dataTypeEnName, ruleType);
    }

}
