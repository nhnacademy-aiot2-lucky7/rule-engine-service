package com.nhnacademy.ruleengineservice.threshold.service.impl;

import com.nhnacademy.ruleengineservice.common.exception.NotFoundException;
import com.nhnacademy.ruleengineservice.gateway.adapter.GatewayAdapter;
import com.nhnacademy.ruleengineservice.sensor.adapter.SensorAdapter;
import com.nhnacademy.ruleengineservice.sensor_rule.service.SensorRuleGenerateService;
import com.nhnacademy.ruleengineservice.threshold.dto.ThresholdAnalysisDTO;
import com.nhnacademy.ruleengineservice.threshold.dto.ThresholdRequest;
import com.nhnacademy.ruleengineservice.threshold.service.ThresholdRuleProcessorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ThresholdRuleProcessorServiceImpl implements ThresholdRuleProcessorService {

    private final SensorRuleGenerateService sensorRuleGenerateService;
    private final SensorAdapter sensorAdapter;
    private final GatewayAdapter gatewayAdapter;

    @Override
    public void generateRulesFromAnalysis(ThresholdRequest request) {
        String gatewayId = request.getGatewayId();
        List<ThresholdAnalysisDTO> analysisDTOList = sensorAdapter.getAnalysisResult(gatewayId);

        if (analysisDTOList == null || analysisDTOList.isEmpty()) {
            throw new NotFoundException(String.format("게이트웨이 [%s]에 대한 분석 결과가 없거나 가져오기에 실패했습니다.", gatewayId));
        }
        sensorRuleGenerateService.generateRules(analysisDTOList);

        gatewayAdapter.activateGateway(gatewayId);
    }
}
