package com.nhnacademy.ruleengineservice.sensor_rule.service;

import com.nhnacademy.ruleengineservice.enums.ActionType;
import com.nhnacademy.ruleengineservice.enums.Operator;
import com.nhnacademy.ruleengineservice.enums.RuleType;
import com.nhnacademy.ruleengineservice.sensor_data.dto.DataDTO;
import com.nhnacademy.ruleengineservice.sensor_rule.domain.SensorRule;
import com.nhnacademy.ruleengineservice.sensor_rule.service.impl.SensorRuleViolationServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SensorRuleViolationServiceImplTest {

    @InjectMocks
    private SensorRuleViolationServiceImpl sensorRuleViolationService;

    @Mock
    private SensorRuleService sensorRuleService;

    private SensorRule maxRule;
    private SensorRule minRule;
    private SensorRule avgRule;

    @BeforeEach
    void setUp() {
        minRule = SensorRule.builder()
                .gatewayId(1L)
                .sensorId("sensor1")
                .dataTypeEnName("temperature")
                .dataTypeKrName("온도")
                .ruleType(RuleType.MIN)
                .operator(Operator.LESS_THAN)
                .value(22.0)
                .action(ActionType.LOG_WARNING)
                .build();

        maxRule = SensorRule.builder()
                .gatewayId(1L)
                .sensorId("sensor1")
                .dataTypeEnName("temperature")
                .dataTypeKrName("온도")
                .ruleType(RuleType.MAX)
                .operator(Operator.GREATER_THAN)
                .value(30.0)
                .action(ActionType.LOG_WARNING)
                .build();

        avgRule = SensorRule.builder()
                .gatewayId(1L)
                .sensorId("sensor1")
                .dataTypeEnName("temperature")
                .dataTypeKrName("온도")
                .ruleType(RuleType.AVG)
                .operator(Operator.OUT_OF_BOUND)
                .value(25.0)
                .minValue(20.0)
                .maxValue(30.0)
                .action(ActionType.LOG_WARNING)
                .build();
    }

    @Test
    @DisplayName("모든 룰 통과 확인")
    void allRulesPassedSuccessfully() {
        Long gatewayId = 1L;
        String sensorId = "sensor1";
        String dataType = "temperature";
        double sensorValue = 28.0; // 모든 룰을 만족하는 값

        DataDTO dataDTO = new DataDTO(
                gatewayId,
                sensorId,
                dataType,
                sensorValue,
                20250505L
        );

        when(sensorRuleService.getSensorRule(gatewayId, sensorId, dataType, RuleType.MIN)).thenReturn(minRule);
        when(sensorRuleService.getSensorRule(gatewayId, sensorId, dataType, RuleType.MAX)).thenReturn(maxRule);
        when(sensorRuleService.getSensorRule(gatewayId, sensorId, dataType, RuleType.AVG)).thenReturn(avgRule);

        List<SensorRule> violatedRules = sensorRuleViolationService.getViolatedRules(dataDTO);

        assertThat(violatedRules).isEmpty();
        verify(sensorRuleService, times(3)).getSensorRule(anyLong(), anyString(), anyString(), any());
    }


    @Test
    @DisplayName("MAX 룰 위반 확인")
    void allRulesPassCheck() {
        Long gatewayId = 1L;
        String sensorId = "sensor1";
        String dataType = "temperature";
        double sensorValue = 33.0;

        DataDTO dataDTO = new DataDTO(
                gatewayId,
                sensorId,
                dataType,
                sensorValue,
                20250505L
        );

        when(sensorRuleService.getSensorRule(gatewayId, sensorId, dataType, RuleType.MIN)).thenReturn(minRule);
        when(sensorRuleService.getSensorRule(gatewayId, sensorId, dataType, RuleType.MAX)).thenReturn(maxRule);
        when(sensorRuleService.getSensorRule(gatewayId, sensorId, dataType, RuleType.AVG)).thenReturn(avgRule);

        List<SensorRule> violatedRules = sensorRuleViolationService.getViolatedRules(dataDTO);

        assertThat(violatedRules).containsExactlyInAnyOrder(maxRule, avgRule);
        verify(sensorRuleService, times(3)).getSensorRule(anyLong(), anyString(), anyString(), any());
    }

    @Test
    @DisplayName("룰이 없을 경우 룰 위반도 없음")
    void noSensorRules() {
        DataDTO dataDTO = new DataDTO(
                1L,
                "sensor1",
                "temperature",
                35.0,
                20250505L
        );

        // mock: getSensorRule은 null 반환
        when(sensorRuleService.getSensorRule(anyLong(), anyString(), anyString(), any())).thenReturn(null);

        // when
        List<SensorRule> result = sensorRuleViolationService.getViolatedRules(dataDTO);

        // then
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }
}