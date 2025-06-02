package com.nhnacademy.ruleengineservice.threshold.service.impl;

import com.nhnacademy.ruleengineservice.common.exception.NotFoundException;
import com.nhnacademy.ruleengineservice.gateway.adapter.GatewayAdapter;
import com.nhnacademy.ruleengineservice.sensor.adapter.SensorAdapter;
import com.nhnacademy.ruleengineservice.sensor_rule.service.SensorRuleGenerateService;
import com.nhnacademy.ruleengineservice.sensor_rule.service.SensorRuleService;
import com.nhnacademy.ruleengineservice.threshold.dto.ThresholdAnalysisDTO;
import com.nhnacademy.ruleengineservice.threshold.dto.ThresholdRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ThresholdRuleProcessorServiceImplTest {

    @Mock
    private SensorRuleGenerateService sensorRuleGenerateService;

    @Mock
    private SensorRuleService sensorRuleService;

    @Mock
    private SensorAdapter sensorAdapter;

    @Mock
    private GatewayAdapter gatewayAdapter;

    @InjectMocks
    private ThresholdRuleProcessorServiceImpl thresholdRuleProcessorService;

    private static List<ThresholdAnalysisDTO> analysisDTOList;


    @BeforeEach
    void setUp() {
        analysisDTOList = new ArrayList<>();

        ThresholdAnalysisDTO analysisDTO1 = new ThresholdAnalysisDTO(
                1L,
                "sensor1",
                "temperature",
                "온도",
                20.00,
                55.00,
                35.00,
                19.00,
                34.00
        );
        analysisDTOList.add(analysisDTO1);
    }

    @Test
    @DisplayName("게이트웨이 아이디로 분석된 값을 가져와 룰 생성하기")
    void testGenerateRulesFromAnalysis() {
        Long gatewayId = 1L;
        String status = "분석완료";

        ThresholdRequest request = new ThresholdRequest();
        request.setGatewayId(gatewayId);
        request.setStatus(status);

        when(sensorAdapter.getAnalysisResult(gatewayId)).thenReturn(analysisDTOList);

        thresholdRuleProcessorService.generateRulesFromAnalysis(request);

        verify(sensorAdapter).getAnalysisResult(gatewayId);
        verify(sensorRuleGenerateService).generateRules(analysisDTOList);
        verify(gatewayAdapter).activateGateway(gatewayId);
    }

    @Test
    @DisplayName("해당 게이트웨이 아이디에 분석된 값이 없거나 가져오기 실패 - 예외 발생")
    void testGenerateRulesFromAnalysis_NotFound() {
        Long gatewayId = 1L;
        String status = "분석완료";

        ThresholdRequest request = new ThresholdRequest();
        request.setGatewayId(gatewayId);
        request.setStatus(status);

        when(sensorAdapter.getAnalysisResult(gatewayId)).thenReturn(new ArrayList<>());

        assertThatThrownBy(() ->
                thresholdRuleProcessorService.generateRulesFromAnalysis(request)
        ).isInstanceOf(NotFoundException.class)
                .hasMessageContaining("게이트웨이 [gateway1]에 대한 분석 결과가 없거나 가져오기에 실패했습니다.");
    }
}