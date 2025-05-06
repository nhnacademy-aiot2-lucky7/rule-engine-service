package com.nhnacademy.ruleengineservice.enums;

/**
 * {@code Operator}는 센서 값과 기준 값 사이의 비교 연산을 정의하는 열거형입니다.
 * <p>
 * 각 연산자는 센서 값과 기준 값 사이의 비교를 수행하며, 결과를 {@code boolean} 값으로 반환합니다.
 * </p>
 * <ul>
 *     <li>{@code GREATER_THAN} - 센서 값이 기준 값보다 큰 경우</li>
 *     <li>{@code LESS_THAN} - 센서 값이 기준 값보다 작은 경우</li>
 *     <li>{@code EQUAL} - 센서 값이 기준 값과 같은 경우</li>
 * </ul>
 */
public enum Operator {
    GREATER_THAN(">", "초과하면") {
        @Override
        public boolean compare(double sensorValue, double targetValue) {
            return sensorValue > targetValue;
        }
    },
    LESS_THAN("<", "미만이면") {
        @Override
        public boolean compare(double sensorValue, double targetValue) {
            return sensorValue < targetValue;
        }
    },
    EQUAL("=", "이면") {
        @Override
        public boolean compare(double sensorValue, double targetValue) {
            return sensorValue == targetValue;
        }
    };

    private final String symbol;
    private final String description;

    Operator(String symbol, String description) {
        this.symbol = symbol;
        this.description = description;
    }

    /**
     * 센서 값과 기준 값을 비교하는 추상 메소드입니다.
     *
     * @param sensorValue 센서에서 가져온 값
     * @param targetValue 비교할 기준 값
     * @return 비교 결과 (true 또는 false)
     */
    public abstract boolean compare(double sensorValue, double targetValue);

    /**
     * 연산자의 기호를 반환합니다.
     *
     * @return 연산자의 기호
     */
    public String getSymbol() {
        return symbol;
    }

    /**
     * 연산자의 설명을 반환합니다.
     *
     * @return 연산자의 설명
     */
    public String getDescription() {
        return description;
    }
}
