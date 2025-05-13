package com.nhnacademy.ruleengineservice.sensor_rule.service.impl;

import com.nhnacademy.ruleengineservice.enums.ActionType;
import com.nhnacademy.ruleengineservice.enums.Operator;
import com.nhnacademy.ruleengineservice.enums.RuleType;
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
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class SensorRuleGenerateServiceImplTest {

    @Mock
    private SensorRuleService sensorRuleService;

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
                25.00,
                70.00,
                50.00,
                15.00,
                75.00
        );
        analysisDTOList.add(analysisDTO1);
        analysisDTOList.add(analysisDTO2);
    }

    @Test
    @DisplayName("여러 데이터 타입을 받았을 경우")
    void testGenerateRules() {

        sensorRuleGenerateService.generateRules(analysisDTOList);

        ArgumentCaptor<SensorRule> captor = ArgumentCaptor.forClass(SensorRule.class);
        verify(sensorRuleService, times(6)).saveSensorRule(captor.capture());

        List<SensorRule> rules = captor.getAllValues();

        Assertions.assertAll(
                () -> {
                    assertTrue(rules.stream().anyMatch(rule ->
                            rule.getGatewayId().equals("gateway1")
                                    && rule.getSensorId().equals("sensor1")
                                    && rule.getDataType().equals("temperature")
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
                                    && rule.getDataType().equals("temperature")
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
                                    && rule.getDataType().equals("temperature")
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
                                    && rule.getDataType().equals("humidity")
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
                                    && rule.getDataType().equals("humidity")
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
                                    && rule.getDataType().equals("humidity")
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