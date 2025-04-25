package com.nhnacademy.ruleengineservice.common.controller;

import com.nhnacademy.ruleengineservice.sensorrule.domain.SensorAnalysisResult;
import com.nhnacademy.ruleengineservice.sensorrule.repository.SensorAnalysisResultRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/analysis")
public class AnalysisResultController {

    private final SensorAnalysisResultRepository analysisRepository;
    private final RuleRedisService ruleRedisService;

    /**
     * 분석 완료 후 Python 측에서 ID를 전달하면,
     * MySQL에서 해당 ID의 분석 결과를 조회하고,
     * 룰을 생성하여 Redis에 저장합니다.
     *
     * @param payload {"id": 123} 형태의 JSON
     * @return 200 OK
     */
    @PostMapping("/complete")
    public ResponseEntity<Void> receiveAnalysisComplete(@RequestBody Map<String, Object> payload) {
        Long id = Long.parseLong(payload.get("id").toString());
        log.info("분석 완료된 ID 수신: {}", id);

        SensorAnalysisResult result = analysisRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 ID의 분석 결과 없음"));

        ruleRedisService.saveRulesToRedis(result);

        log.info("Redis에 룰 저장 완료 - Sensor ID: {}", result.getSensorId());
        return ResponseEntity.ok().build();
    }
}