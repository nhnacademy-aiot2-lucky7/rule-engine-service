package com.nhnacademy.ruleengineservice.common.filter;

import com.nhnacademy.ruleengineservice.enums.Operator;
import com.nhnacademy.ruleengineservice.enums.RuleType;
import com.nhnacademy.ruleengineservice.sensorrule.domain.Rule;
import com.nhnacademy.ruleengineservice.sensorrule.service.SensorRuleService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.*;

class SensorRuleViolationCheckerTest {

    @InjectMocks
    private SensorRuleViolationChecker sensorRuleViolationChecker;

    @Mock
    private SensorRuleService sensorRuleService;

    @Mock
    private Rule rule;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("룰 통과 확인")
    void allRulesPassCheck() {
        String gatewayId = "gateway1";
        String sensorId = "sensor1";
        String dataType = "temperature";
        double sensorValue = 26.5;

        Map<String, Object> sensorData = new HashMap<>();
        sensorData.put("gatewayId", gatewayId);
        sensorData.put("sensorId", sensorId);
        sensorData.put("dataType", dataType);
        sensorData.put("value", sensorValue);

        when(sensorRuleService.getRulesByKey(gatewayId, sensorId, dataType)).thenReturn(List.of(rule));

        // Rule 값 및 Operator를 mock 처리
        when(rule.getValue()).thenReturn(20.0); // 기준값 설정
        when(rule.getRuletype()).thenReturn(RuleType.MIN);
        when(rule.getOperator()).thenReturn(Operator.LESS_THAN); // 비교할 연산자 설정

        boolean comparisonResult = Operator.LESS_THAN.compare(sensorValue, 20.0); // 실제 비교 작업

        List<Rule> violatedRules = sensorRuleViolationChecker.getViolatedRules(sensorData);

        Assertions.assertFalse(comparisonResult);
        Assertions.assertTrue(violatedRules.isEmpty());

        verify(sensorRuleService, times(1)).getRulesByKey(gatewayId, sensorId, dataType);
        verify(rule, times(1)).getValue();
        verify(rule, times(1)).getOperator();
    }

    @Test
    @DisplayName("필터 불통 확인")
    void testFilter_failsAtFirstRule() {
        String gatewayId = "gateway1";
        String sensorId = "sensor1";
        String dataType = "temperature";
        double sensorValue = 70.0;

        Map<String, Object> sensorData = new HashMap<>();
        sensorData.put("gatewayId", gatewayId);
        sensorData.put("sensorId", sensorId);
        sensorData.put("dataType", dataType);
        sensorData.put("value", sensorValue);

        when(sensorRuleService.getRulesByKey(gatewayId, sensorId, dataType)).thenReturn(List.of(rule)); // 룰 리스트 반환
        when(rule.getValue()).thenReturn(60.0); // 기준값 설정
        when(rule.getRuletype()).thenReturn(RuleType.MAX);
        when(rule.getOperator()).thenReturn(Operator.GREATER_THAN); // 실제 enum 객체 사용


        boolean comparisonResult = Operator.GREATER_THAN.compare(sensorValue, 60.0);

        List<Rule> violatedRules = sensorRuleViolationChecker.getViolatedRules(sensorData);

        Assertions.assertTrue(comparisonResult);
        Assertions.assertFalse(violatedRules.isEmpty());

        // verify()로 호출 여부 확인
        verify(sensorRuleService, times(1)).getRulesByKey(gatewayId, sensorId, dataType); // 올바른 인자 사용 확인
        verify(rule, times(1)).getValue();
        verify(rule, times(1)).getOperator();
    }
}