package com.nhnacademy.ruleengineservice.sensor_rule.service;

import com.nhnacademy.ruleengineservice.enums.RuleType;
import com.nhnacademy.ruleengineservice.enums.SaveStatus;
import com.nhnacademy.ruleengineservice.sensor_rule.domain.SensorRule;

/**
 * 센서 룰 서비스 인터페이스입니다.
 * 센서에 대한 룰을 저장하고 조회하는 기능을 제공합니다.
 * <p>
 * 이 인터페이스는 룰 엔진 시스템에서 센서 룰을 Redis나 다른 저장소에 저장하고, 이를 조회하는 메소드들을 정의합니다.
 * </p>
 */
public interface SensorRuleService {

    SaveStatus saveSensorRule(SensorRule sensorRule);
    SensorRule getSensorRule(String gatewayId, String sensorId, String dataType, RuleType ruleType);
    void updateSensorRule(SensorRule sensorRule);
    void deleteSensorRule(String gatewayId, String sensorId, String dataType, RuleType ruleType);
}
