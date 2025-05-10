package com.nhnacademy.ruleengineservice.sensor_rule.service.impl;

import com.nhnacademy.ruleengineservice.common.exception.NotFoundException;
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
    private static final String EXCEPTION_MESSAGE = "%s, %s, %s에 해당하는 룰이 없습니다.";

    @Override
    public void saveSensorRule(SensorRule sensorRule) {
        String key = sensorRule.getRedisKey();
        if(Boolean.TRUE.equals(redisTemplate.hasKey(key))) {
            log.info("기존 룰이 존재합니다. 덮어씁니다. key={}", key);
        } else {
            log.info("신규 룰을 저장합니다. key={}", key);
        }

        redisTemplate.opsForValue().set(key, sensorRule);
    }

    @Override
    public SensorRule getSensorRule(String gatewayId, String sensorId, String dataType, RuleType ruleType) {
        String key = String.format("rule:gateway:%s:sensor:%s:%s:%s", gatewayId, sensorId, dataType, ruleType);
        SensorRule rule = redisTemplate.opsForValue().get(key);

        if (rule == null) {
            throw new NotFoundException(String.format(EXCEPTION_MESSAGE, gatewayId, sensorId, dataType));
        }

        return rule;
    }

    @Override
    public void updateSensorRule(SensorRule sensorRule) {
        String key = sensorRule.getRedisKey();
        SensorRule existing = redisTemplate.opsForValue().get(key);

        if (existing == null) {
            throw new NotFoundException(String.format(EXCEPTION_MESSAGE, sensorRule.getGatewayId(), sensorRule.getSensorId(), sensorRule.getDataType()));
        }

        redisTemplate.opsForValue().set(key, sensorRule);
        log.info("Updated sensor rule: {}", key);
    }

    @Override
    public void deleteSensorRule(String gatewayId, String sensorId, String dataType, String ruleType) {
        String key = String.format("rule:gateway:%s:sensor:%s:%s:%s", gatewayId, sensorId, dataType, ruleType);
        Boolean deleted = redisTemplate.delete(key);

        if (!deleted) {
            throw new NotFoundException(String.format(EXCEPTION_MESSAGE, gatewayId, sensorId, dataType));
        }

        log.info("Deleted sensor rule: {}", key);
    }
}