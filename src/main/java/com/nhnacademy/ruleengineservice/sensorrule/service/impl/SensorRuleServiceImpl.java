/*
package com.nhnacademy.ruleengineservice.sensorrule.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.ruleengineservice.sensorrule.domain.SensorAnalysisResult;
import com.nhnacademy.ruleengineservice.sensorrule.repository.SensorAnalysisResultRepository;
import com.nhnacademy.ruleengineservice.sensorrule.service.SensorRuleService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SensorRuleServiceImpl implements SensorRuleService {

    private final SensorAnalysisResultRepository resultRepository;
    private final RedisTemplate<String, Object> redisTemplate;
    private final ObjectMapper objectMapper;

    public void saveSensorRules(Long id) {
        SensorAnalysisResult analysisResult = resultRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 ID의 분석 결과 없음"));

        String key = String.format("rule:sensor:%s:%s", analysisResult.getSensorId(), analysisResult.getDataType());


    }
}
*/
