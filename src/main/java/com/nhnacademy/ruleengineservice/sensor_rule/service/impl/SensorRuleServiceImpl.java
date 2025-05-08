package com.nhnacademy.ruleengineservice.sensor_rule.service.impl;

import com.nhnacademy.ruleengineservice.common.exception.SensorRuleNotFoundException;
import com.nhnacademy.ruleengineservice.enums.RuleType;
import com.nhnacademy.ruleengineservice.sensor_rule.domain.SensorRule;
import com.nhnacademy.ruleengineservice.sensor_rule.service.SensorRuleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class SensorRuleServiceImpl implements SensorRuleService {

    private final RedisTemplate<String, SensorRule> redisTemplate;

    @Override
    public void saveSensorRule(SensorRule sensorRule) {
        String key = sensorRule.getRedisKey();
        redisTemplate.opsForValue().set(key, sensorRule);
        log.info("Saved sensor rule: {}", key);
    }

    @Override
    public SensorRule getSensorRule(String gatewayId, String sensorId, String dataType, RuleType ruleType) {
        String key = String.format("rule:gateway:%s:sensor:%s:%s:%s", gatewayId, sensorId, dataType, ruleType);
        SensorRule rule = redisTemplate.opsForValue().get(key);

        if (rule == null) {
            throw new SensorRuleNotFoundException(sensorId, dataType);
        }

        return rule;
    }

    @Override
    public void updateSensorRule(SensorRule sensorRule) {
        String key = sensorRule.getRedisKey();
        SensorRule existing = redisTemplate.opsForValue().get(key);

        if (existing == null) {
            throw new SensorRuleNotFoundException(sensorRule.getSensorId(), sensorRule.getDataType());
        }

        redisTemplate.opsForValue().set(key, sensorRule);
        log.info("Updated sensor rule: {}", key);
    }

    @Override
    public void deleteSensorRule(String gatewayId, String sensorId, String dataType, String ruleType) {
        String key = String.format("rule:gateway:%s:sensor:%s:%s:%s", gatewayId, sensorId, dataType, ruleType);
        Boolean deleted = redisTemplate.delete(key);

        if (!deleted) {
            throw new SensorRuleNotFoundException(sensorId, dataType);
        }

        log.info("Deleted sensor rule: {}", key);
    }
}