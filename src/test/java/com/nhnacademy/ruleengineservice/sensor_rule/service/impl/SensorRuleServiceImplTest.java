package com.nhnacademy.ruleengineservice.sensor_rule.service.impl;

import com.nhnacademy.ruleengineservice.common.exception.NotFoundException;
import com.nhnacademy.ruleengineservice.enums.ActionType;
import com.nhnacademy.ruleengineservice.enums.Operator;
import com.nhnacademy.ruleengineservice.enums.RuleType;
import com.nhnacademy.ruleengineservice.sensor_rule.domain.SensorRule;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SensorRuleServiceImplTest {

    @Mock
    private RedisTemplate<String, SensorRule> redisTemplate;

    @Mock
    private ValueOperations<String, SensorRule> valueOperations;

    @InjectMocks
    private SensorRuleServiceImpl sensorRuleService;

    private SensorRule sensorRule;

    @BeforeEach
    void setUp() {
        sensorRule = SensorRule.createRule(
                "gateway1", "sensor1", "temperature",
                RuleType.MAX, Operator.GREATER_THAN, 60.0, null,null, ActionType.LOG_WARNING
        );

        lenient().when(redisTemplate.opsForValue()).thenReturn(valueOperations);
    }

    @Test
    @DisplayName("saveSensorRule: 센서 룰 저장 성공")
    void saveSensorRule_shouldSaveToRedis() {
        sensorRuleService.saveSensorRule(sensorRule);

        verify(valueOperations).set(sensorRule.getRedisKey(), sensorRule);
    }

    @Test
    @DisplayName("getSensorRule: 센서 룰 조회 성공")
    void getSensorRule_shouldReturnRule_whenExists() {
        String key = sensorRule.getRedisKey();

        when(redisTemplate.hasKey(key)).thenReturn(true);
        when(valueOperations.get(key)).thenReturn(sensorRule);

        SensorRule result = sensorRuleService.getSensorRule(
                "gateway1", "sensor1", "temperature", RuleType.MAX
        );

        Assertions.assertEquals(sensorRule, result);
    }

    @Test
    @DisplayName("getSensorRule: 센서 룰 존재하지 않아 예외 발생")
    void getSensorRule_shouldThrowException_whenNotFound() {
        String key = sensorRule.getRedisKey();

        when(redisTemplate.hasKey(key)).thenReturn(null);

        assertThatThrownBy(() ->
                sensorRuleService.getSensorRule("gateway1", "sensor1", "temperature", RuleType.MAX)
        ).isInstanceOf(NotFoundException.class)
                .hasMessageContaining("해당 센서 룰을 찾을 수 없습니다. - Gateway: gateway1, Sensor: sensor1, DataType: temperature");
    }

    @Test
    @DisplayName("updateSensorRule: 기존 룰이 존재하여 업데이트 성공")
    void updateSensorRule_shouldUpdate_whenExists() {
        String key = sensorRule.getRedisKey();
        when(valueOperations.get(key)).thenReturn(sensorRule);

        sensorRuleService.updateSensorRule(sensorRule);

        verify(valueOperations).set(key, sensorRule);
    }

    @Test
    @DisplayName("updateSensorRule: 기존 룰이 없어 예외 발생")
    void updateSensorRule_shouldThrowException_whenNotFound() {
        String key = sensorRule.getRedisKey();
        when(valueOperations.get(key)).thenReturn(null);

        assertThatThrownBy(() ->
                sensorRuleService.updateSensorRule(sensorRule)
        ).isInstanceOf(NotFoundException.class)
                .hasMessageContaining("해당 센서 룰을 찾을 수 없습니다. - Gateway: gateway1, Sensor: sensor1, DataType: temperature");
    }

    @Test
    @DisplayName("deleteSensorRule: 룰 삭제 성공")
    void deleteSensorRule_shouldDeleteSuccessfully() {
        String key = sensorRule.getRedisKey();

        when(redisTemplate.hasKey(key)).thenReturn(true);
        when(redisTemplate.delete(key)).thenReturn(true);

        sensorRuleService.deleteSensorRule("gateway1", "sensor1", "temperature", "MAX");

        verify(redisTemplate).delete(key);
    }

    @Test
    @DisplayName("deleteSensorRule: 룰 삭제 시 조회 실패로 예외 발생")
    void deleteSensorRule_shouldThrowException_whenDeleteFails() {
        String key = sensorRule.getRedisKey();

        when(redisTemplate.hasKey(key)).thenReturn(false);

        assertThatThrownBy(() ->
                sensorRuleService.deleteSensorRule("gateway1", "sensor1", "temperature", "MAX")
        ).isInstanceOf(NotFoundException.class)
                .hasMessageContaining("해당 센서 룰을 찾을 수 없습니다. - Gateway: gateway1, Sensor: sensor1, DataType: temperature");
    }
}