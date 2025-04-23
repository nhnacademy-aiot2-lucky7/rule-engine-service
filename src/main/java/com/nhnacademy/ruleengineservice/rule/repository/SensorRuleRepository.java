package com.nhnacademy.ruleengineservice.rule.repository;

import com.nhnacademy.ruleengineservice.rule.dto.SensorRuleDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class SensorRuleRepository {

    private final RedisTemplate<String, SensorRuleDto> redisTemplate;

    @Autowired
    public SensorRuleRepository(RedisTemplate<String, SensorRuleDto> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    // 키 생성: sensorId와 sensorType을 모두 고려
    private String generateKey(String sensorId, String sensorType) {
        return "sensor:rule:" + sensorId + ":" + sensorType.toLowerCase();
    }

    // 룰 저장
    public void saveRule(String sensorId, String sensorType, SensorRuleDto ruleDto) {
        redisTemplate.opsForValue().set(generateKey(sensorId, sensorType), ruleDto);
    }

    // 룰 조회
    public SensorRuleDto getRule(String sensorId, String sensorType) {
        return redisTemplate.opsForValue().get(generateKey(sensorId, sensorType));
    }

    // 룰 삭제
    public void deleteRule(String sensorId, String sensorType) {
        redisTemplate.delete(generateKey(sensorId, sensorType));
    }

    // 키 존재 여부 확인
    public boolean exists(String sensorId, String sensorType) {
        return redisTemplate.hasKey(generateKey(sensorId, sensorType));
    }
}
