package com.nhnacademy.ruleengineservice.sensor_rule.service.impl;

import com.nhnacademy.ruleengineservice.common.exception.SensorRuleException;
import com.nhnacademy.ruleengineservice.dashboard.dto.RuleDeleteRequest;
import com.nhnacademy.ruleengineservice.event.dto.ViolatedRuleEventDTO;
import com.nhnacademy.ruleengineservice.event.producer.EventProducer;
import com.nhnacademy.ruleengineservice.sensor_rule.service.SensorRuleDeleteService;
import com.nhnacademy.ruleengineservice.sensor_rule.service.SensorRuleService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class SensorRuleDeleteServiceImplTest {

    @Mock
    private SensorRuleService sensorRuleService;

    @Mock
    private EventProducer eventProducer;

    @InjectMocks
    private SensorRuleDeleteServiceImpl sensorRuleDeleteService;

    @Test
    void deleteRules_throwsException_whenRequiredFieldsMissing() {
        // given
        RuleDeleteRequest invalidRequest = new RuleDeleteRequest();
        // 일부 필드만 채워서 유효성 검증 실패 유도
        invalidRequest.setSensorId("sensor-1");

        // expect
        assertThatThrownBy(() -> sensorRuleDeleteService.deleteRules(invalidRequest))
                .isInstanceOf(SensorRuleException.class)
                .hasMessageContaining("필수값이 누락되었습니다");
    }

    @Test
    void deleteRules_callsDeleteSensorAllRulesAndSendEvent() {
        // given
        RuleDeleteRequest validRequest = new RuleDeleteRequest();
        validRequest.setGatewayId(1L);
        validRequest.setSensorId("sensor-123");
        validRequest.setDepartmentId("dept-456");
        validRequest.setDataTypeEnName("temperature");
        validRequest.setDataTypeKrName("온도");

        // when
        sensorRuleDeleteService.deleteRules(validRequest);

        // then
        verify(sensorRuleService).deleteSensorAllRules(1L, "sensor-123", "temperature");
        verify(eventProducer).sendEvent(any()); // 이벤트도 발행되는지 확인
    }

    @Test
    void deleteRules_sendsCorrectEventDetail() {
        // given
        RuleDeleteRequest validRequest = new RuleDeleteRequest();
        validRequest.setGatewayId(1L);
        validRequest.setSensorId("sensor-789");
        validRequest.setDepartmentId("dep-001");
        validRequest.setDataTypeEnName("humidity");
        validRequest.setDataTypeKrName("습도");

        // when
        sensorRuleDeleteService.deleteRules(validRequest);

        // then
        verify(eventProducer, times(1)).sendEvent(any(ViolatedRuleEventDTO.class));
    }
}