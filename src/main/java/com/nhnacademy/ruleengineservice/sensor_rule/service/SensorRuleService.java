package com.nhnacademy.ruleengineservice.sensor_rule.service;

import com.nhnacademy.ruleengineservice.sensor_rule.domain.Rule;

import java.util.List;

/**
 * 센서 룰 서비스 인터페이스입니다.
 * 센서에 대한 룰을 저장하고 조회하는 기능을 제공합니다.
 * <p>
 * 이 인터페이스는 룰 엔진 시스템에서 센서 룰을 Redis나 다른 저장소에 저장하고, 이를 조회하는 메소드들을 정의합니다.
 * </p>
 */
public interface SensorRuleService {

    /**
     * 지정된 게이트웨이, 장치, 데이터 타입에 대한 룰을 저장합니다.
     * <p>
     * 이 메소드는 주어진 게이트웨이, 장치, 데이터 타입에 맞는 룰을 저장하며, 이미 존재하는 경우 룰을 업데이트합니다.
     * </p>
     *
     * @param gatewayId  센서가 연결된 게이트웨이의 ID
     * @param deviceId   센서 또는 장치의 ID
     * @param dataType   데이터 타입 (예: 온도, 습도 등)
     * @param rule       새로 저장할 룰 객체
     */
    void saveSensorRules(String gatewayId, String deviceId, String dataType, Rule rule);

    /**
     * 주어진 게이트웨이, 장치, 데이터 타입에 해당하는 룰을 조회합니다.
     *
     * @param gatewayId  센서가 연결된 게이트웨이의 ID
     * @param deviceId   센서 또는 장치의 ID
     * @param dataType   데이터 타입
     * @return 해당하는 룰 리스트
     */
    List<Rule> getRulesByKey(String gatewayId, String deviceId, String dataType);
}
