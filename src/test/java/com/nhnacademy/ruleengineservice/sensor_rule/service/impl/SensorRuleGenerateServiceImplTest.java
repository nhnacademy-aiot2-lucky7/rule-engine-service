package com.nhnacademy.ruleengineservice.sensor_rule.service.impl;

import com.nhnacademy.ruleengineservice.enums.ActionType;
import com.nhnacademy.ruleengineservice.enums.Operator;
import com.nhnacademy.ruleengineservice.enums.RuleType;
import com.nhnacademy.ruleengineservice.enums.SaveStatus;
import com.nhnacademy.ruleengineservice.event.dto.ViolatedRuleEventDTO;
import com.nhnacademy.ruleengineservice.event.producer.EventProducer;
import com.nhnacademy.ruleengineservice.gateway.adapter.GatewayAdapter;
import com.nhnacademy.ruleengineservice.sensor_rule.domain.SensorRule;
import com.nhnacademy.ruleengineservice.sensor_rule.service.SensorRuleService;
import com.nhnacademy.ruleengineservice.threshold.dto.ThresholdAnalysisDTO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SensorRuleGenerateServiceImplTest {

    @Mock
    private SensorRuleService sensorRuleService;

    @Mock
    private GatewayAdapter gatewayAdapter;

    @Mock
    private EventProducer eventProducer;

    @InjectMocks
    private SensorRuleGenerateServiceImpl sensorRuleGenerateService;

    private static List<ThresholdAnalysisDTO> analysisDTOList;

    @BeforeEach
    void setUp() {
        analysisDTOList = new ArrayList<>();

        ThresholdAnalysisDTO analysisDTO1 = new ThresholdAnalysisDTO(
                "gateway1",
                "sensor1",
                "temperature",
                "온도",
                20.00,
                55.00,
                35.00,
                19.00,
                34.00
        );
        ThresholdAnalysisDTO analysisDTO2 = new ThresholdAnalysisDTO(
                "gateway1",
                "sensor1",
                "humidity",
                "습도",
                25.00,
                70.00,
                50.00,
                15.00,
                75.00
        );
        analysisDTOList = Arrays.asList(analysisDTO1, analysisDTO2);
    }

    @Test
    @DisplayName("모든 임계치가 있는 경우 - 데이터 타입별 룰 생성 및 이벤트메세지 발행")
    void allRulesGeneratedAndEventPublished() {

        when(sensorRuleService.saveSensorRule(any(SensorRule.class))).thenReturn(SaveStatus.NEW);
        when(gatewayAdapter.getDepartmentIdByGatewayId(anyString())).thenReturn("부서-01");

        sensorRuleGenerateService.generateRules(analysisDTOList);

        verify(sensorRuleService, times(6)).saveSensorRule(any(SensorRule.class));
        verify(eventProducer, times(2)).sendEvent(any(ViolatedRuleEventDTO.class));
    }

    @Test
    @DisplayName("기존 룰이 있는 경우 - 새로운 룰로 수정")
    void rulesGeneratedAndEventPublishedOnUpdated() {

        when(sensorRuleService.saveSensorRule(any(SensorRule.class))).thenReturn(SaveStatus.UPDATED);
        when(gatewayAdapter.getDepartmentIdByGatewayId(anyString())).thenReturn("부서-01");

        sensorRuleGenerateService.generateRules(analysisDTOList);

        verify(sensorRuleService, times(6)).saveSensorRule(any(SensorRule.class));  // 룰이 6번 저장되어야 합니다.
        verify(eventProducer, times(2)).sendEvent(any(ViolatedRuleEventDTO.class));  // 이벤트가 2번 발행되어야 합니다.
    }

    @Test
    @DisplayName("모든 룰 생성 확인 - 자세한 값 확인")
    void checkEventDetails() {

        when(gatewayAdapter.getDepartmentIdByGatewayId(anyString())).thenReturn(anyString());

        sensorRuleGenerateService.generateRules(analysisDTOList);

        ArgumentCaptor<SensorRule> captor = ArgumentCaptor.forClass(SensorRule.class);
        verify(sensorRuleService, times(6)).saveSensorRule(captor.capture());

        List<SensorRule> rules = captor.getAllValues();

        Assertions.assertAll(
                () -> {
                    assertTrue(rules.stream().anyMatch(rule ->
                            rule.getGatewayId().equals("gateway1")
                                    && rule.getSensorId().equals("sensor1")
                                    && rule.getDataTypeEnName().equals("temperature")
                                    && rule.getDataTypeKrName().equals("온도")
                                    && rule.getRuleType().equals(RuleType.MIN)
                                    && rule.getOperator().equals(Operator.LESS_THAN)
                                    && rule.getValue().equals(20.00)
                                    && Objects.isNull(rule.getMinValue())
                                    && Objects.isNull(rule.getMaxValue())
                                    && rule.getAction().equals(ActionType.SEND_ALERT)
                    ));
                },
                () -> {
                    assertTrue(rules.stream().anyMatch(rule ->
                            rule.getGatewayId().equals("gateway1")
                                    && rule.getSensorId().equals("sensor1")
                                    && rule.getDataTypeEnName().equals("temperature")
                                    && rule.getDataTypeKrName().equals("온도")
                                    && rule.getRuleType().equals(RuleType.MAX)
                                    && rule.getOperator().equals(Operator.GREATER_THAN)
                                    && rule.getValue().equals(55.00)
                                    && Objects.isNull(rule.getMinValue())
                                    && Objects.isNull(rule.getMaxValue())
                                    && rule.getAction().equals(ActionType.SEND_ALERT)
                    ));
                },
                () -> {
                    assertTrue(rules.stream().anyMatch(rule ->
                            rule.getGatewayId().equals("gateway1")
                                    && rule.getSensorId().equals("sensor1")
                                    && rule.getDataTypeEnName().equals("temperature")
                                    && rule.getDataTypeKrName().equals("온도")
                                    && rule.getRuleType().equals(RuleType.AVG)
                                    && rule.getOperator().equals(Operator.OUT_OF_BOUND)
                                    && rule.getValue().equals(35.00)
                                    && rule.getMinValue().equals(19.00)
                                    && rule.getMaxValue().equals(34.00)
                                    && rule.getAction().equals(ActionType.SEND_ALERT)
                    ));
                },
                () -> {
                    assertTrue(rules.stream().anyMatch(rule ->
                            rule.getGatewayId().equals("gateway1")
                                    && rule.getSensorId().equals("sensor1")
                                    && rule.getDataTypeEnName().equals("humidity")
                                    && rule.getDataTypeKrName().equals("습도")
                                    && rule.getRuleType().equals(RuleType.MIN)
                                    && rule.getOperator().equals(Operator.LESS_THAN)
                                    && rule.getValue().equals(25.00)
                                    && Objects.isNull(rule.getMinValue())
                                    && Objects.isNull(rule.getMaxValue())
                                    && rule.getAction().equals(ActionType.SEND_ALERT)
                    ));
                },
                () -> {
                    assertTrue(rules.stream().anyMatch(rule ->
                            rule.getGatewayId().equals("gateway1")
                                    && rule.getSensorId().equals("sensor1")
                                    && rule.getDataTypeEnName().equals("humidity")
                                    && rule.getDataTypeKrName().equals("습도")
                                    && rule.getRuleType().equals(RuleType.MAX)
                                    && rule.getOperator().equals(Operator.GREATER_THAN)
                                    && rule.getValue().equals(70.00)
                                    && Objects.isNull(rule.getMinValue())
                                    && Objects.isNull(rule.getMaxValue())
                                    && rule.getAction().equals(ActionType.SEND_ALERT)
                    ));
                },
                () -> {
                    assertTrue(rules.stream().anyMatch(rule ->
                            rule.getGatewayId().equals("gateway1")
                                    && rule.getSensorId().equals("sensor1")
                                    && rule.getDataTypeEnName().equals("humidity")
                                    && rule.getDataTypeKrName().equals("습도")
                                    && rule.getRuleType().equals(RuleType.AVG)
                                    && rule.getOperator().equals(Operator.OUT_OF_BOUND)
                                    && rule.getValue().equals(50.00)
                                    && rule.getMinValue().equals(15.00)
                                    && rule.getMaxValue().equals(75.00)
                                    && rule.getAction().equals(ActionType.SEND_ALERT)
                    ));
                }
        );
    }
}