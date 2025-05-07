package com.nhnacademy.ruleengineservice.sensor_rule.service.impl;

import com.nhnacademy.ruleengineservice.common.exception.SensorRuleNotFoundException;
import com.nhnacademy.ruleengineservice.enums.ActionType;
import com.nhnacademy.ruleengineservice.enums.DataType;
import com.nhnacademy.ruleengineservice.enums.Operator;
import com.nhnacademy.ruleengineservice.enums.RuleType;
import com.nhnacademy.ruleengineservice.sensor_rule.domain.Rule;
import com.nhnacademy.ruleengineservice.sensor_rule.domain.SensorRule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SensorRuleServiceImplTest {

    @Mock
    private RedisTemplate<String, SensorRule> redisTemplate;

    @Mock
    private ValueOperations<String, SensorRule> valueOperations;

    @InjectMocks
    private SensorRuleServiceImpl sensorRuleService;

    private Rule existingRule;
    private Rule newRule;
    private SensorRule sensorRule;
    private String gatewayId = "gateway1";
    private String deviceId = "sensor1";
    private String dataType = "temperature";
    private String key = "rule:gateway:" + gatewayId + ":sensor:" + deviceId + ":" + dataType;

    @BeforeEach
    void setUp() {
        existingRule = Rule.createRule(
                DataType.TEMPERATURE,
                RuleType.MAX,
                Operator.GREATER_THAN,
                50.0, 10.0,
                ActionType.LOG_WARNING);

        newRule = Rule.createRule(
                DataType.TEMPERATURE,
                RuleType.MAX,
                Operator.GREATER_THAN,
                60.0,
                5.0,
                ActionType.LOG_WARNING
        );

        // 기본 SensorRule 설정
        sensorRule = SensorRule.ofNewRule(key, existingRule);
    }

    @Test
    @DisplayName("Redis에서 룰을 가져오는지 확인")
    void testSaveSensorRules_NewRule() {
        // Redis에서 룰을 가져오는 부분을 모킹
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
        when(valueOperations.get(key)).thenReturn(null); // 기존 룰이 없을 때

        // 새 룰을 추가하는 메서드 호출
        sensorRuleService.saveSensorRules(gatewayId, deviceId, dataType, newRule);

        // 새 SensorRule이 Redis에 저장된 것을 검증
        verify(redisTemplate.opsForValue()).set(eq(key), any(SensorRule.class));
    }

    @Test
    @DisplayName("Redis에서 기존 저장된 룰 가져오는지 확인")
    void testSaveSensorRules_ExistingRule_Update() {
        // Redis에서 기존 룰을 가져오는 부분을 모킹
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
        when(valueOperations.get(key)).thenReturn(sensorRule); // 기존 룰이 있을 때

        // 기존 룰을 업데이트하는 메서드 호출
        sensorRuleService.saveSensorRules(gatewayId, deviceId, dataType, newRule);

        // 수정된 룰이 Redis에 저장된 것을 검증
        verify(redisTemplate.opsForValue()).set(eq(key), any(SensorRule.class));
    }

    @Test
    @DisplayName("Redis에서 룰을 가져오는지 확인")
    void testGetRulesByKey_Found() {
        // Redis에서 SensorRule을 가져오는 부분을 모킹
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
        when(valueOperations.get(key)).thenReturn(sensorRule); // 룰이 있을 때

        // 룰을 조회하는 메서드 호출
        List<Rule> rules = sensorRuleService.getRulesByKey(gatewayId, deviceId, dataType);

        // 룰이 잘 조회되는지 검증
        assertNotNull(rules);
        assertEquals(1, rules.size());
        assertEquals(existingRule, rules.get(0));
    }

    @Test
    @DisplayName("redis에서 룰을 찾지 못한 경우 - 예외처리")
    void testGetRulesByKey_NotFound() {
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
        when(valueOperations.get(key)).thenReturn(null); // 룰이 없을 때

        // 예외가 발생하는지 확인
        SensorRuleNotFoundException exception = assertThrows(SensorRuleNotFoundException.class, () -> {
            sensorRuleService.getRulesByKey(gatewayId, deviceId, dataType);
        });

        // 예외 메시지 검증
        assertEquals("No rules found for deviceId: sensor1 and dataType: temperature", exception.getMessage());
    }
}
