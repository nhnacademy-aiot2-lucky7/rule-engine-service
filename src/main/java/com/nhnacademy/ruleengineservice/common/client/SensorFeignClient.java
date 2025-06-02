package com.nhnacademy.ruleengineservice.common.client;


import com.nhnacademy.ruleengineservice.threshold.dto.ThresholdAnalysisDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "sensor-service", url = "${sensor.service.url}")
public interface SensorFeignClient {

    @GetMapping("/sensor-service/api/v1/threshold-histories/{gateway-id}")
    List<ThresholdAnalysisDTO> getAnalysisResult(@PathVariable("gateway-id") Long gatewayId);
}