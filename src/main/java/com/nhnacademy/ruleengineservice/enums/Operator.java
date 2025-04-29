package com.nhnacademy.ruleengineservice.enums;

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
    GREATER_THAN_OR_EQUAL(">=", "이상이면") {
        @Override
        public boolean compare(double sensorValue, double targetValue) {
            return sensorValue >= targetValue;
        }
    },
    LESS_THAN_OR_EQUAL("<=", "이하면") {
        @Override
        public boolean compare(double sensorValue, double targetValue) {
            return sensorValue <= targetValue;
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

    // 각 Operator가 비교를 스스로 하게 만든다
    public abstract boolean compare(double sensorValue, double targetValue);

    public String getSymbol() {
        return symbol;
    }

    public String getDescription() {
        return description;
    }
}
