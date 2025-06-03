package com.nhnacademy.ruleengineservice.sensor_rule.service.impl;

import com.nhnacademy.ruleengineservice.dashboard.dto.RuleCreateRequest;
import com.nhnacademy.ruleengineservice.enums.ActionType;
import com.nhnacademy.ruleengineservice.enums.Operator;
import com.nhnacademy.ruleengineservice.enums.RuleType;
import com.nhnacademy.ruleengineservice.enums.SaveStatus;
import com.nhnacademy.ruleengineservice.event.dto.ViolatedRuleEventDTO;
import com.nhnacademy.ruleengineservice.event.producer.EventProducer;
import com.nhnacademy.ruleengineservice.sensor_rule.domain.SensorRule;
import com.nhnacademy.ruleengineservice.sensor_rule.service.SensorRuleService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SensorRuleGenerateServiceImplTest {

    @Mock
    private SensorRuleService sensorRuleService;

    @Mock
    private EventProducer eventProducer;

    @InjectMocks
    private SensorRuleGenerateServiceImpl sensorRuleGenerateService;

    private static RuleCreateRequest createRequest;

    @BeforeEach
    void setUp() {
        Long gatewayId = 1L;
        String sensorId = "sensor1";
        String departmentId = "부서1";
        String dataTypeEnName = "temperature";
        String dataTypeKrName = "온도";
        Double thresholdMin = 10.00;
        Double thresholdMax = 40.00;

        createRequest = new RuleCreateRequest();
        createRequest.setGatewayId(gatewayId);
        createRequest.setSensorId(sensorId);
        createRequest.setDepartmentId(departmentId);
        createRequest.setDataTypeEnName(dataTypeEnName);
        createRequest.setDataTypeKrName(dataTypeKrName);
        createRequest.setThresholdMin(thresholdMin);
        createRequest.setThresholdMax(thresholdMax);
    }

    @Test
    @DisplayName("모든 임계치가 있는 경우 - 데이터 타입별 룰 생성 및 이벤트메세지 발행")
    void allRulesGeneratedAndEventPublished() {

        when(sensorRuleService.saveSensorRule(any(SensorRule.class))).thenReturn(SaveStatus.NEW);

        sensorRuleGenerateService.generateRules(createRequest);

        verify(sensorRuleService, times(2)).saveSensorRule(any(SensorRule.class));
        verify(eventProducer, times(1)).sendEvent(any(ViolatedRuleEventDTO.class));
    }

    @Test
    @DisplayName("기존 룰이 있는 경우 - 새로운 룰로 수정")
    void rulesGeneratedAndEventPublishedOnUpdated() {

        when(sensorRuleService.saveSensorRule(any(SensorRule.class))).thenReturn(SaveStatus.UPDATED);

        sensorRuleGenerateService.generateRules(createRequest);

        verify(sensorRuleService, times(2)).saveSensorRule(any(SensorRule.class));
        verify(eventProducer, times(1)).sendEvent(any(ViolatedRuleEventDTO.class));
    }

    @Test
    @DisplayName("모든 룰 생성 확인 - 자세한 값 확인")
    void checkEventDetails() {
        sensorRuleGenerateService.generateRules(createRequest);

        ArgumentCaptor<SensorRule> captor = ArgumentCaptor.forClass(SensorRule.class);
        verify(sensorRuleService, times(2)).saveSensorRule(captor.capture());

        List<SensorRule> rules = captor.getAllValues();

        Assertions.assertAll(
                () -> {
                    assertTrue(rules.stream().anyMatch(rule ->
                            rule.getGatewayId().equals(1L)
                                    && rule.getSensorId().equals("sensor1")
                                    && rule.getDepartmentId().equals("부서1")
                                    && rule.getDataTypeEnName().equals("temperature")
                                    && rule.getDataTypeKrName().equals("온도")
                                    && rule.getRuleType().equals(RuleType.MIN)
                                    && rule.getOperator().equals(Operator.LESS_THAN)
                                    && rule.getValue().equals(10.00)
                                    && Objects.isNull(rule.getMinValue())
                                    && Objects.isNull(rule.getMaxValue())
                                    && rule.getAction().equals(ActionType.SEND_ALERT)
                    ));
                },
                () -> {
                    assertTrue(rules.stream().anyMatch(rule ->
                            rule.getGatewayId().equals(1L)
                                    && rule.getSensorId().equals("sensor1")
                                    && rule.getDepartmentId().equals("부서1")
                                    && rule.getDataTypeEnName().equals("temperature")
                                    && rule.getDataTypeKrName().equals("온도")
                                    && rule.getRuleType().equals(RuleType.MAX)
                                    && rule.getOperator().equals(Operator.GREATER_THAN)
                                    && rule.getValue().equals(40.00)
                                    && Objects.isNull(rule.getMinValue())
                                    && Objects.isNull(rule.getMaxValue())
                                    && rule.getAction().equals(ActionType.SEND_ALERT)
                    ));
                }
        );
    }
}