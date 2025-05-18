package com.nhnacademy.ruleengineservice.integration_test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.ruleengineservice.enums.ActionType;
import com.nhnacademy.ruleengineservice.enums.Operator;
import com.nhnacademy.ruleengineservice.enums.RuleType;
import com.nhnacademy.ruleengineservice.event.producer.EventProducer;
import com.nhnacademy.ruleengineservice.sensor_data.dto.DataDTO;
import com.nhnacademy.ruleengineservice.sensor_rule.domain.SensorRule;
import com.nhnacademy.ruleengineservice.sensor_rule.service.SensorRuleService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class SensorDataIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private SensorRuleService sensorRuleService;

    @Autowired
    private RedisTemplate<String, SensorRule> redisTemplate;

    @MockitoSpyBean
    private EventProducer eventProducer;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private final String gatewayId = "gateway-001";
    private final String sensorId = "sensor-001";
    private final String dataType = "temperature";

    @BeforeEach
    void setUp() {
        redisTemplate.getConnectionFactory().getConnection().flushAll(); // Redis 초기화

        // MIN: 통과되는 룰
        sensorRuleService.saveSensorRule(SensorRule.builder()
                .gatewayId(gatewayId)
                .sensorId(sensorId)
                .dataType(dataType)
                .ruleType(RuleType.MIN)
                .operator(Operator.LESS_THAN)
                .value(10.00)
                .action(ActionType.SEND_ALERT)
                .build());

        // MAX: 위반되는 룰
        sensorRuleService.saveSensorRule(SensorRule.builder()
                .gatewayId(gatewayId)
                .sensorId(sensorId)
                .dataType(dataType)
                .ruleType(RuleType.MAX)
                .operator(Operator.GREATER_THAN)
                .value(30.0)
                .action(ActionType.SEND_ALERT)
                .build());

        // AVG: 위반되는 룰
        sensorRuleService.saveSensorRule(SensorRule.builder()
                .gatewayId(gatewayId)
                .sensorId(sensorId)
                .dataType(dataType)
                .ruleType(RuleType.AVG)
                .operator(Operator.OUT_OF_BOUND)
                .value(20.00)
                .minValue(8.00)
                .maxValue(29.00)
                .action(ActionType.SEND_ALERT)
                .build());
    }

    @Test
    void whenDataViolatesMaxAndAvgRules_thenSendTwoEvents() throws Exception {
        DataDTO data = new DataDTO(
                gatewayId,
                sensorId,
                dataType,
                31.00,
                20250520L
        );

        mockMvc.perform(post("/rule_engine/data")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(data)))
                .andExpect(status().isOk());

        verify(eventProducer, times(2)).sendEvent(Mockito.any());
    }
}
