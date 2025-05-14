package com.nhnacademy.ruleengineservice.sensor_rule.service.impl;

import com.nhnacademy.ruleengineservice.common.exception.NotFoundException;
import com.nhnacademy.ruleengineservice.enums.RuleType;
import com.nhnacademy.ruleengineservice.enums.SaveStatus;
import com.nhnacademy.ruleengineservice.sensor_rule.domain.SensorRule;
import com.nhnacademy.ruleengineservice.sensor_rule.service.SensorRuleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class SensorRuleServiceImpl implements SensorRuleService {

    private final RedisTemplate<String, SensorRule> redisTemplate;
    private static final String EXCEPTION_MESSAGE = "해당 센서 룰을 찾을 수 없습니다. - Gateway: %s, Sensor: %s, DataType: %s";

    @Override
    public SaveStatus saveSensorRule(SensorRule sensorRule) {
        String key = sensorRule.getRedisKey();
        Boolean exists = redisTemplate.hasKey(key);

        SaveStatus status = (exists == null || !exists) ? SaveStatus.NEW : SaveStatus.UPDATED;

        if(status == SaveStatus.NEW) {
            log.info("신규 룰을 저장합니다. key={}", key);
        } else {
            log.info("기존 룰이 존재합니다. 덮어씁니다. key={}", key);
        }

        redisTemplate.opsForValue().set(key, sensorRule);
        return status;
    }

    @Override
    public SensorRule getSensorRule(String gatewayId, String sensorId, String dataType, RuleType ruleType) {
        String key = String.format("rule:gateway:%s:sensor:%s:%s:%s", gatewayId, sensorId, dataType, ruleType);
        Boolean exists = redisTemplate.hasKey(key);

        if(exists == null || !exists) {
            throw new NotFoundException(String.format(EXCEPTION_MESSAGE, gatewayId, sensorId, dataType));
        }

        return redisTemplate.opsForValue().get(key);
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
        Boolean exists = redisTemplate.hasKey(key);

        if (exists == null || ! exists) {
            throw new NotFoundException(String.format(EXCEPTION_MESSAGE, gatewayId, sensorId, dataType));
        }
        redisTemplate.delete(key);
    }
}