package com.nhnacademy.ruleengineservice.integration_test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.ruleengineservice.dashboard.dto.RuleCreateRequest;
import com.nhnacademy.ruleengineservice.event.dto.ViolatedRuleEventDTO;
import com.nhnacademy.ruleengineservice.event.producer.EventProducer;
import com.nhnacademy.ruleengineservice.sensor_rule.domain.SensorRule;
import com.nhnacademy.ruleengineservice.sensor_rule.service.SensorRuleService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class DashboardControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private RedisTemplate<String, SensorRule> redisTemplate;

    @MockitoBean
    private SensorRuleService sensorRuleService;

    @MockitoBean
    private EventProducer eventProducer;

    @Test
    @DisplayName("통합테스트 - 룰 생성 성공")
    void testCreateRuleSuccess() throws Exception {
        // given
        RuleCreateRequest request = new RuleCreateRequest();

        request.setGatewayId(1L);
        request.setSensorId("sensor-001");
        request.setDepartmentId("부서1");
        request.setDataTypeEnName("temperature");
        request.setDataTypeKrName("온도");
        request.setThresholdMin(10.00);
        request.setThresholdMax(30.00);

        String json = objectMapper.writeValueAsString(request);

        mockMvc.perform(post("/rule_engine/rules/create_rule")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated());

        verify(sensorRuleService, times(2)).saveSensorRule(any(SensorRule.class));

        ArgumentCaptor<ViolatedRuleEventDTO> eventCaptor = ArgumentCaptor.forClass(ViolatedRuleEventDTO.class);
        verify(eventProducer).sendEvent(eventCaptor.capture());

        ViolatedRuleEventDTO sentEvent = eventCaptor.getValue();
        assertThat(sentEvent.getEventDetails()).contains("센서 [sensor-001]의 [온도] 데이터에 대한 룰이 생성되었습니다.");
        assertThat(sentEvent.getDepartmentId()).isEqualTo("부서1");
        assertThat(sentEvent.getSourceId()).isEqualTo("sensor-001");
    }
}
