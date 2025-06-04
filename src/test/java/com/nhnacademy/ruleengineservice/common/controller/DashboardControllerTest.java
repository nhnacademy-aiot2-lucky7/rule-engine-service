package com.nhnacademy.ruleengineservice.common.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.ruleengineservice.dashboard.dto.RuleCreateRequest;
import com.nhnacademy.ruleengineservice.dashboard.service.DashboardRuleProcessorService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = DashboardController.class)
class DashboardControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private DashboardRuleProcessorService processorService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("룰 생성 - 성공")
    void testCreateRule() throws Exception{
        RuleCreateRequest request = new RuleCreateRequest();
        request.setGatewayId(1L);
        request.setSensorId("sensor-001");
        request.setDepartmentId("부서1");
        request.setDataTypeEnName("temperature");
        request.setDataTypeKrName("온도");
        request.setThresholdMin(10.00);
        request.setThresholdMax(30.00);

        mockMvc.perform(post("/rule_engine/rules/create_rule")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());
    }

}