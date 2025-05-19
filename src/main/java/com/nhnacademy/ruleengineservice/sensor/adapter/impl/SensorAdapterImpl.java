package com.nhnacademy.ruleengineservice.sensor.adapter.impl;

import com.nhnacademy.ruleengineservice.sensor.adapter.SensorAdapter;
import com.nhnacademy.ruleengineservice.common.client.SensorFeignClient;
import com.nhnacademy.ruleengineservice.threshold.dto.ThresholdAnalysisDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Slf4j
@Component
public class SensorAdapterImpl implements SensorAdapter {

    private final SensorFeignClient sensorFeignClient;

    public SensorAdapterImpl(SensorFeignClient sensorFeignClient) {
        this.sensorFeignClient = sensorFeignClient;
    }

    @Override
    public List<ThresholdAnalysisDTO> getAnalysisResult(String gatewayId) {
        try {
            return sensorFeignClient.getAnalysisResult(gatewayId);
        } catch (Exception e) {
            log.warn("[Feign] 센서 서비스 요청 실패: {}", e.getMessage(), e);
            return Collections.emptyList();
        }
    }
}
