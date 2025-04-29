package com.nhnacademy.ruleengineservice.common.filter;

import com.nhnacademy.ruleengineservice.enums.Operator;
import com.nhnacademy.ruleengineservice.sensorrule.domain.Rule;
import com.nhnacademy.ruleengineservice.sensorrule.domain.SensorRule;
import com.nhnacademy.ruleengineservice.sensorrule.service.impl.SensorRuleServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


class SensorRuleFilterChainTest {

    @InjectMocks
    private SensorRuleFilterChain sensorRuleFilterChain;

    @Mock
    private SensorRuleServiceImpl sensorRuleService;

    @Mock
    private Rule rule;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("필터 통과 확인")
    void testFilter_passAllRules() {
        String deviceId = "sensor1";
        String dataType = "temperature";
        double sensorValue = 26.5;

        Map<String, Object> sensorData = new HashMap<>();
        sensorData.put("deviceId", deviceId);
        sensorData.put("dataType", dataType);
        sensorData.put("value", sensorValue);

        // Mock behavior: getRulesByKey 호출 시 deviceId와 dataType 인자를 정확하게 설정
        when(sensorRuleService.getRulesByKey(deviceId, dataType)).thenReturn(Arrays.asList(rule));

        // Rule 값 및 Operator를 mock 처리
        when(rule.getValue()).thenReturn(20.0); // 기준값 설정
        when(rule.getOperator()).thenReturn(Operator.LESS_THAN); // 비교할 연산자 설정

        // Operator.LESS_THAN.compare는 실제 비교 작업을 해야 하므로 mock 없이 테스트에서 비교될 수 있도록 설정
        // Operator 비교가 제대로 작동하도록 설정
        boolean comparisonResult = Operator.LESS_THAN.compare(sensorValue, 20.0); // 실제 비교 작업

        // When: 필터가 제대로 작동하는지 확인
        boolean result = sensorRuleFilterChain.filter(sensorData);

        // Then: 필터가 통과해야 함
        assert(result);

        // verify()로 호출 여부 확인
        verify(sensorRuleService, times(1)).getRulesByKey(deviceId, dataType);
        verify(rule, times(1)).getValue();
        verify(rule, times(1)).getOperator();
    }
}