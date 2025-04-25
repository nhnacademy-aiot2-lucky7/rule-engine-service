package com.nhnacademy.ruleengineservice.sensorrule.repository;

import com.nhnacademy.ruleengineservice.enums.ActionType;
import com.nhnacademy.ruleengineservice.enums.Operator;
import com.nhnacademy.ruleengineservice.enums.RuleType;
import com.nhnacademy.ruleengineservice.enums.SensorType;
import com.nhnacademy.ruleengineservice.sensorrule.domain.SensorRule;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.util.Collections;

import static org.mockito.Mockito.*;

class SensorRuleRepositoryTest {

    private RedisTemplate<String, SensorRule> redisTemplate;
    private ValueOperations<String, SensorRule> valueOperations;
    private SensorRuleRepository sensorRuleRepository;

    @BeforeEach
    void setUp() {
        redisTemplate = mock(RedisTemplate.class);
        valueOperations = mock(ValueOperations.class);
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
        sensorRuleRepository = new SensorRuleRepository(redisTemplate);
    }

    @Test
    @DisplayName("saveRule 호출 시 RedisTemplate의 set이 호출되는지 확인")
    void testSaveRule() {
        SensorRule.Rule rule = SensorRule.Rule.builder()
                .type(RuleType.THRESHOLD)
                .operator(Operator.GREATER_THAN)
                .value(30.0)
                .action(ActionType.SEND_ALERT)
                .build();

        SensorRule ruleDto = SensorRule.builder()
                .sensorId("sensor-1001")
                .sensorType(SensorType.TEMPERATURE)
                .rules(Collections.singletonList(rule))
                .build();

        sensorRuleRepository.saveRule("sensor-1001", "TEMPERATURE", ruleDto);

        verify(valueOperations, times(1))
                .set("sensor:rule:sensor-1001:temperature", ruleDto);
    }

    @Test
    @DisplayName("getRule 호출 시 RedisTemplate의 get이 호출되고 값이 반환되는지 확인")
    void testGetRule() {
        SensorRule ruleDto = SensorRule.builder()
                .sensorId("sensor-1001")
                .sensorType(SensorType.HUMIDITY)
                .rules(Collections.emptyList())
                .build();

        when(valueOperations.get("sensor:rule:sensor-1001:humidity")).thenReturn(ruleDto);

        SensorRule result = sensorRuleRepository.getRule("sensor-1001", "HUMIDITY");

        Assertions.assertEquals(ruleDto, result);
    }

    @Test
    @DisplayName("deleteRule 호출 시 RedisTemplate의 delete가 호출되는지 확인")
    void testDeleteRule() {
        sensorRuleRepository.deleteRule("sensor-1001", "temperature");

        verify(redisTemplate, times(1))
                .delete("sensor:rule:sensor-1001:temperature");
    }

    @Test
    @DisplayName("exists 호출 시 RedisTemplate의 hasKey가 호출되고 결과가 반환되는지 확인")
    void testExists() {
        when(redisTemplate.hasKey("sensor:rule:sensor-1001:humidity")).thenReturn(true);

        boolean exists = sensorRuleRepository.exists("sensor-1001", "humidity");

        Assertions.assertTrue(exists);
    }
}
