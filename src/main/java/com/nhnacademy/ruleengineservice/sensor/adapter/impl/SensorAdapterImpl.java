package com.nhnacademy.ruleengineservice.sensor.adapter.impl;

import com.nhnacademy.ruleengineservice.sensor.adapter.SensorAdapter;
import com.nhnacademy.ruleengineservice.threshold.dto.ThresholdAnalysisDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientException;

@Slf4j
@Component
public class SensorAdapterImpl implements SensorAdapter {

    private final WebClient sensorWebClient;

    public SensorAdapterImpl(@Qualifier("sensorWebClient") WebClient sensorWebClient) {
        this.sensorWebClient = sensorWebClient;
    }

    public ThresholdAnalysisDTO getAnalysisResult(String gatewayId) {
        try {
            return sensorWebClient.get()
                    .uri("/sensor-service/analysis-result/{gatewayId}", gatewayId)
                    .retrieve()
                    .bodyToMono(ThresholdAnalysisDTO.class)
                    .block(); // 동기적으로 호출
        } catch (WebClientException e) {
            log.error("[센서 서비스] 분석 결과 요청 실패: {}", e.getMessage(), e);
            return null;
        }
    }
}
