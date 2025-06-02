package com.nhnacademy.ruleengineservice.integration_test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.ruleengineservice.enums.SaveStatus;
import com.nhnacademy.ruleengineservice.event.dto.ViolatedRuleEventDTO;
import com.nhnacademy.ruleengineservice.event.producer.EventProducer;
import com.nhnacademy.ruleengineservice.gateway.adapter.GatewayAdapter;
import com.nhnacademy.ruleengineservice.sensor.adapter.SensorAdapter;
import com.nhnacademy.ruleengineservice.sensor_rule.domain.SensorRule;
import com.nhnacademy.ruleengineservice.sensor_rule.service.SensorRuleService;
import com.nhnacademy.ruleengineservice.threshold.dto.ThresholdAnalysisDTO;
import com.nhnacademy.ruleengineservice.threshold.dto.ThresholdRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class ThresholdIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private SensorAdapter sensorAdapter;

    @MockitoBean
    private GatewayAdapter gatewayAdapter;

    @MockitoBean
    private SensorRuleService sensorRuleService;

    @MockitoBean
    private EventProducer eventProducer;

    @Test
    @DisplayName("데이터 분석외 완료되었을 때 분석 데이터를 받아와 룰 생성")
    void thresholdWebhook_shouldProcessSuccessfully() throws Exception {
        // Given
        String gatewayId = "gateway-001";
        ThresholdRequest request = new ThresholdRequest();
        request.setGatewayId(gatewayId);
        request.setStatus("분석완료");

        ThresholdAnalysisDTO analysis = new ThresholdAnalysisDTO(
                gatewayId,
                "sensor-001",
                "temperature",
                "온도",
                20.00,
                50.00,
                30.00,
                15.00,
                40.00
        );

        when(sensorAdapter.getAnalysisResult(gatewayId)).thenReturn(List.of(analysis));
        when(sensorRuleService.saveSensorRule(any(SensorRule.class))).thenReturn(SaveStatus.NEW);
        when(gatewayAdapter.getDepartmentIdByGatewayId(gatewayId)).thenReturn("1");

        // When & Then
        mockMvc.perform(post("/rule_engine/webhook/threshold_complete")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());

        // Verify rule save
        verify(sensorRuleService, times(3)).saveSensorRule(any(SensorRule.class));

        // Verify gateway activation
        verify(gatewayAdapter).activateGateway(gatewayId);

        // Verify event sent
        ArgumentCaptor<ViolatedRuleEventDTO> eventCaptor = ArgumentCaptor.forClass(ViolatedRuleEventDTO.class);
        verify(eventProducer).sendEvent(eventCaptor.capture());

        ViolatedRuleEventDTO sentEvent = eventCaptor.getValue();
        assertThat(sentEvent.getEventDetails()).contains("센서 [sensor-001]의 [온도] 데이터에 대한 룰이 생성되었습니다.");
        assertThat(sentEvent.getDepartmentId()).isEqualTo("1");
        assertThat(sentEvent.getSourceId()).isEqualTo("sensor-001");
    }
}
