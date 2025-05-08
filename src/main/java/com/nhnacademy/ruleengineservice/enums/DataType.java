package com.nhnacademy.ruleengineservice.enums;

/**
 * {@code DataType}은 센서 데이터의 유형을 정의하는 열거형입니다.
 * <p>
 * 이 열거형은 센서 데이터에서 처리할 수 있는 다양한 데이터 유형을 구분합니다.
 * 각 데이터 유형은 문자열 값과 그에 대한 설명을 가지고 있습니다.
 * </p>
 *
 * <p>데이터 유형:</p>
 * <ul>
 *     <li>{@code TEMPERATURE} - 온도를 나타내는 데이터 유형</li>
 *     <li>{@code HUMIDITY} - 습도를 나타내는 데이터 유형</li>
 *     <li>{@code POWER} - 전력 소비를 나타내는 데이터 유형</li>
 * </ul>
 *
 */
public enum DataType {

    /**
     * 온도 데이터를 나타내는 데이터 유형.
     */
    TEMPERATURE("temperature", "온도"),

    /**
     * 습도 데이터를 나타내는 데이터 유형.
     */
    HUMIDITY("humidity", "습도"),

    /**
     * 전력 소비 데이터를 나타내는 데이터 유형.
     */
    POWER("power", "전력");

    private final String value;
    private final String desc;

    /**
     * 생성자
     *
     * @param value 데이터 유형의 값
     * @param desc 데이터 유형에 대한 설명
     */
    DataType(String value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    /**
     * 데이터 유형의 값을 반환합니다.
     *
     * @return 데이터 유형의 값
     */
    public String getValue() {
        return value;
    }

    /**
     * 데이터 유형에 대한 설명을 반환합니다.
     *
     * @return 데이터 유형에 대한 설명
     */
    public String getDesc() {
        return desc;
    }
}