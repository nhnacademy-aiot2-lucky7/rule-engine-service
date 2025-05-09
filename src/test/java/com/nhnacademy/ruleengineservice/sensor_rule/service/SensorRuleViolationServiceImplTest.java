package com.nhnacademy.ruleengineservice.sensor_rule.service;

import com.nhnacademy.ruleengineservice.common.exception.SensorRuleNotFoundException;
import com.nhnacademy.ruleengineservice.enums.ActionType;
import com.nhnacademy.ruleengineservice.enums.Operator;
import com.nhnacademy.ruleengineservice.enums.RuleType;
import com.nhnacademy.ruleengineservice.sensor_data.dto.DataDTO;
import com.nhnacademy.ruleengineservice.sensor_rule.domain.SensorRule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

class SensorRuleViolationServiceImplTest {

    @InjectMocks
    private SensorRuleViolationService sensorRuleViolationService;

    @Mock
    private SensorRuleService sensorRuleService;

    private SensorRule maxRule;
    private SensorRule minRule;
    private SensorRule avgRule;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        maxRule = SensorRule.createRule(
                "gateway1",
                "sensor1",
                "temperature",
                RuleType.MAX,
                Operator.GREATER_THAN,
                30.0,
                10.0,
                ActionType.LOG_WARNING
        );

        minRule = SensorRule.createRule(
                "gateway1",
                "sensor1",
                "temperature",
                RuleType.MIN,
                Operator.LESS_THAN,
                27.0,
                10.0,
                ActionType.LOG_WARNING
        );

        avgRule = SensorRule.createRule(
                "gateway1",
                "sensor1",
                "temperature",
                RuleType.AVG,
                Operator.LESS_THAN,
                25.0,
                10.0,
                ActionType.LOG_WARNING
        );
    }

    @Test
    @DisplayName("모든 룰 통과 확인")
    void allRulesPassedSuccessfully() {
        String gatewayId = "gateway1";
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

        when(sensorRuleService.getSensorRule(gatewayId, sensorId, dataType, RuleType.MAX)).thenReturn(maxRule);
        when(sensorRuleService.getSensorRule(gatewayId, sensorId, dataType, RuleType.MIN)).thenReturn(minRule);
        when(sensorRuleService.getSensorRule(gatewayId, sensorId, dataType, RuleType.AVG)).thenReturn(avgRule);

        List<SensorRule> violatedRules = sensorRuleViolationService.getViolatedRules(dataDTO);

        assertThat(violatedRules).isEmpty();
        verify(sensorRuleService, times(3)).getSensorRule(anyString(), anyString(), anyString(), any());
    }


    @Test
    @DisplayName("MAX 룰 위반 확인")
    void allRulesPassCheck() {
        String gatewayId = "gateway1";
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

        when(sensorRuleService.getSensorRule(gatewayId, sensorId, dataType, RuleType.MAX)).thenReturn(maxRule);
        when(sensorRuleService.getSensorRule(gatewayId, sensorId, dataType, RuleType.MIN)).thenReturn(minRule);
        when(sensorRuleService.getSensorRule(gatewayId, sensorId, dataType, RuleType.AVG)).thenReturn(avgRule);

        List<SensorRule> violatedRules = sensorRuleViolationService.getViolatedRules(dataDTO);

        assertThat(violatedRules).containsExactlyInAnyOrder(maxRule);
        verify(sensorRuleService, times(3)).getSensorRule(anyString(), anyString(), anyString(), any());
    }

    @Test
    @DisplayName("룰이 존재하지 않을 경우 예외 발생")
    void getViolatedRules_shouldThrowException_whenRuleNotFound() {
        DataDTO dataDTO = new DataDTO(
                "gateway1",
                "sensor1",
                "temperature",
                35.0,
                20250505L
        );

        when(sensorRuleService.getSensorRule("gateway-1", "sensor-1", "temperature", RuleType.MIN))
                .thenThrow(new SensorRuleNotFoundException("sensor-1", "temperature"));

        assertThatThrownBy(() -> sensorRuleViolationService.getViolatedRules(dataDTO))
                .isInstanceOf(SensorRuleNotFoundException.class)
                .hasMessageContaining("No rules found for deviceId: sensor1 and dataType: temperature");
    }
}