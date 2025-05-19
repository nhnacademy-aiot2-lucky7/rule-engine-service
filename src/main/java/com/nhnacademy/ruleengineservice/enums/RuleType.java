package com.nhnacademy.ruleengineservice.enums;

/**
 * {@code RuleType}은 센서 데이터에 적용할 수 있는 다양한 룰 유형을 정의하는 열거형입니다.
 * <p>
 * 이 열거형은 각 센서 데이터에 대해 임계값, 평균값, 최소값, 최댓값과 같은 조건을 설정하여 데이터를 비교하고 분석하는 데 사용됩니다.
 * </p>
 * <ul>
 *     <li>{@code AVG} - 센서 데이터가 평균값을 기준으로 비교되는 경우</li>
 *     <li>{@code MIN} - 센서 데이터가 최소값을 기준으로 비교되는 경우</li>
 *     <li>{@code MAX} - 센서 데이터가 최댓값을 기준으로 비교되는 경우</li>
 * </ul>
 */
public enum RuleType {
    AVG("avg", "평균값의 범위가"),
    MIN("min","최소값이"),
    MAX("max","최댓값이");

    private final String value;
    private final String desc;

    RuleType(String value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    /**
     * 룰 유형의 값(String)을 반환합니다.
     *
     * @return 룰 유형의 값
     */
    public String getValue() {
        return value;
    }

    /**
     * 룰 유형의 설명을 반환합니다.
     *
     * @return 룰 유형의 설명
     */
    public String getDesc() {
        return desc;
    }
}
