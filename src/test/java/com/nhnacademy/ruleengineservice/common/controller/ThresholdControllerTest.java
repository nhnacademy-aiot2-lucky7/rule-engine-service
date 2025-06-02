package com.nhnacademy.ruleengineservice.common.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.ruleengineservice.threshold.dto.ThresholdRequest;
import com.nhnacademy.ruleengineservice.threshold.service.ThresholdRuleProcessorService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ThresholdController.class)
class ThresholdControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ThresholdRuleProcessorService processorService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("gatewayId와 status가 올바르게 전달되는지 확인")
    void testReceiveThresholdResult() throws Exception{
        Long gatewayId = 1L;
        String status = "분석완료";

        ThresholdRequest request = new ThresholdRequest();
        request.setGatewayId(gatewayId);
        request.setStatus(status);

        mockMvc.perform(post("/rule_engine/webhook/threshold_complete")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andDo(print());

        ArgumentCaptor<ThresholdRequest> requestCaptor = ArgumentCaptor.forClass(ThresholdRequest.class);

        verify(processorService).generateRulesFromAnalysis(requestCaptor.capture());

        ThresholdRequest captorRequest = requestCaptor.getValue();
        assertEquals(1L, captorRequest.getGatewayId());
        assertEquals("분석완료", captorRequest.getStatus());
    }
}