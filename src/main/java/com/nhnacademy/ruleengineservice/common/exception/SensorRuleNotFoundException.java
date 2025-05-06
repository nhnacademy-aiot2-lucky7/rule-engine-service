package com.nhnacademy.ruleengineservice.common.exception;

/**
 * SensorRuleNotFoundException은 특정 디바이스 ID와 데이터 타입에 해당하는 센서 룰이 존재하지 않을 때 발생하는 예외입니다.
 * <p>
 * 이 예외는 룰 엔진이 해당 조건의 룰을 찾지 못했을 때 던져지며,
 * 예외 메시지에는 디바이스 ID와 데이터 타입 정보가 포함됩니다.
 * </p>
 */
public class SensorRuleNotFoundException extends RuntimeException {

    /**
     * SensorRuleNotFoundException 생성자.
     *
     * @param deviceId 센서 디바이스의 ID
     * @param dataType 센서 데이터의 타입
     */
    public SensorRuleNotFoundException(String deviceId, String dataType) {
        super("No rules found for deviceId: " + deviceId + " and dataType: " + dataType);
    }
}
