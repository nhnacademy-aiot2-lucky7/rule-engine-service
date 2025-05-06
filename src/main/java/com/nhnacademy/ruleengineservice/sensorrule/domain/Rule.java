package com.nhnacademy.ruleengineservice.sensorrule.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.nhnacademy.ruleengineservice.enums.ActionType;
import com.nhnacademy.ruleengineservice.enums.DataType;
import com.nhnacademy.ruleengineservice.enums.Operator;
import com.nhnacademy.ruleengineservice.enums.RuleType;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * {@code Rule} 클래스는 센서 데이터와 비교할 룰을 정의하는 클래스입니다.
 * 센서 데이터와 룰을 비교하여 룰 위반 여부를 판단하고, 위반된 경우 지정된 액션을 수행합니다.
 */
@Data
@Builder
@EqualsAndHashCode
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Rule {
    /**
     * 룰이 적용되는 센서 데이터의 타입 (예: 온도, 습도 등)
     */
    private DataType datatype;

    /**
     * 룰의 종류 (예: 임계값, 평균값 등)
     */
    private RuleType ruletype;

    /**
     * 룰을 적용할 때 사용할 연산자 (예: 초과, 미만 등)
     */
    private Operator operator;

    /**
     * 룰이 적용될 기준 값
     */
    private Double value;

    /**
     * 룰에 적용되는 범위 값 (선택적)
     */
    private Double range;

    /**
     * 룰 위반 시 수행할 액션 (예: 알림 전송, 장치 종료 등)
     */
    private ActionType action;

    /**
     * {@link Rule} 객체를 생성하는 정적 팩토리 메소드
     *
     * @param datatype 룰이 적용되는 센서 데이터 타입
     * @param ruletype 룰의 종류
     * @param operator 룰에 적용될 연산자
     * @param value 룰에 적용될 기준 값
     * @param range 룰에 적용되는 범위 값
     * @param action 룰 위반 시 수행할 액션
     * @return 생성된 {@link Rule} 객체
     */
    public static Rule createRule(DataType datatype, RuleType ruletype, Operator operator, Double value, Double range, ActionType action) {
        return Rule.builder()
                .datatype(datatype)
                .ruletype(ruletype)
                .operator(operator)
                .value(value)
                .range(range)
                .action(action)
                .build();
    }
}