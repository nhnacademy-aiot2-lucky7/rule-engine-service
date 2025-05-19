package com.nhnacademy.ruleengineservice.integration_test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.ruleengineservice.enums.ActionType;
import com.nhnacademy.ruleengineservice.enums.EventLevel;
import com.nhnacademy.ruleengineservice.enums.Operator;
import com.nhnacademy.ruleengineservice.enums.RuleType;
import com.nhnacademy.ruleengineservice.event.dto.ViolatedRuleEventDTO;
import com.nhnacademy.ruleengineservice.event.producer.EventProducer;
import com.nhnacademy.ruleengineservice.gateway.adapter.GatewayAdapter;
import com.nhnacademy.ruleengineservice.sensor_data.dto.DataDTO;
import com.nhnacademy.ruleengineservice.sensor_rule.domain.SensorRule;
import com.nhnacademy.ruleengineservice.sensor_rule.service.SensorRuleService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
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

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
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

    @MockitoBean
    private GatewayAdapter gatewayAdapter;

    @MockitoBean
    private EventProducer eventProducer;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private final String gatewayId = "gateway-001";
    private final String sensorId1 = "sensor-001";
    private final String sensorId2 = "sensor-002";
    private final String dataTypeEnName = "temperature";
    private final String dataTypeKrName = "온도";

    @BeforeEach
    void setUp() {
        redisTemplate.getConnectionFactory().getConnection().flushAll(); // Redis 초기화

        SensorRule minRule1 = SensorRule.builder()
                .gatewayId(gatewayId)
                .sensorId(sensorId1)
                .dataTypeEnName(dataTypeEnName)
                .dataTypeKrName(dataTypeKrName)
                .ruleType(RuleType.MIN)
                .operator(Operator.LESS_THAN)
                .action(ActionType.SEND_ALERT)
                .value(10.00)
                .build();

        SensorRule maxRule1 = SensorRule.builder()
                .gatewayId(gatewayId)
                .sensorId(sensorId1)
                .dataTypeEnName(dataTypeEnName)
                .dataTypeKrName(dataTypeKrName)
                .ruleType(RuleType.MAX)
                .operator(Operator.GREATER_THAN)
                .action(ActionType.SEND_ALERT)
                .value(40.00)
                .build();

        SensorRule avgRule1 = SensorRule.builder()
                .gatewayId(gatewayId)
                .sensorId(sensorId1)
                .dataTypeEnName(dataTypeEnName)
                .dataTypeKrName(dataTypeKrName)
                .ruleType(RuleType.AVG)
                .operator(Operator.OUT_OF_BOUND)
                .action(ActionType.SEND_ALERT)
                .value(25.00)
                .minValue(8.00)
                .maxValue(40.00)
                .build();

        SensorRule minRule2 = SensorRule.builder()
                .gatewayId(gatewayId)
                .sensorId(sensorId2)
                .dataTypeEnName(dataTypeEnName)
                .dataTypeKrName(dataTypeKrName)
                .ruleType(RuleType.MIN)
                .operator(Operator.LESS_THAN)
                .action(ActionType.SEND_ALERT)
                .value(10.00)
                .build();

        SensorRule maxRule2 = SensorRule.builder()
                .gatewayId(gatewayId)
                .sensorId(sensorId2)
                .dataTypeEnName(dataTypeEnName)
                .dataTypeKrName(dataTypeKrName)
                .ruleType(RuleType.MAX)
                .operator(Operator.GREATER_THAN)
                .action(ActionType.SEND_ALERT)
                .value(40.00)
                .build();

        SensorRule avgRule2 = SensorRule.builder()
                .gatewayId(gatewayId)
                .sensorId(sensorId2)
                .dataTypeEnName(dataTypeEnName)
                .dataTypeKrName(dataTypeKrName)
                .ruleType(RuleType.AVG)
                .operator(Operator.OUT_OF_BOUND)
                .action(ActionType.SEND_ALERT)
                .value(25.00)
                .minValue(10.00)
                .maxValue(40.00)
                .build();

        sensorRuleService.saveSensorRule(minRule1);
        sensorRuleService.saveSensorRule(maxRule1);
        sensorRuleService.saveSensorRule(avgRule1);
        sensorRuleService.saveSensorRule(minRule2);
        sensorRuleService.saveSensorRule(maxRule2);
        sensorRuleService.saveSensorRule(avgRule2);
    }

    @Test
    @DisplayName("실시간 데이터가 들어왔을 때 룰 판단 - 모두 통과일 경우")
    void whenAllDataPass() throws Exception {
        DataDTO data = new DataDTO(
                gatewayId,
                sensorId1,
                dataTypeEnName,
                35.00,
                20250520L
        );

        when(gatewayAdapter.getDepartmentIdByGatewayId(gatewayId)).thenReturn("서버실");

        mockMvc.perform(post("/rule_engine/data")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(data)))
                .andExpect(status().isOk());

        verify(eventProducer, never()).sendEvent(any());
    }

    @Test
    @DisplayName("실시간 데이터가 들어왔을 때 룰 판단 - MAX, AVG 룰 위반일 경우")
    void whenDataViolatesMaxAndAvgRules_thenSendTwoEvents() throws Exception {
        DataDTO data = new DataDTO(
                gatewayId,
                sensorId1,
                dataTypeEnName,
                41.00,
                20250520L
        );

        when(gatewayAdapter.getDepartmentIdByGatewayId(gatewayId)).thenReturn("서버실");

        mockMvc.perform(post("/rule_engine/data")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(data)))
                .andExpect(status().isOk());

        ArgumentCaptor<ViolatedRuleEventDTO> eventCaptor = ArgumentCaptor.forClass(ViolatedRuleEventDTO.class);
        verify(eventProducer, times(2)).sendEvent(eventCaptor.capture());

        var capturedEvents = eventCaptor.getAllValues();

        Assertions.assertAll(
                () -> assertThat(capturedEvents).hasSize(2),
                () -> assertThat(capturedEvents).anySatisfy(event ->
                        assertThat(event.getEventDetails()).contains(
                                "센서 [sensor-001]의 [온도]데이터에 대한 AVG 룰 위반: 데이터값 41.00은(는) 8.00~40.00 범위를 벗어났으므로 알림전송.")
                ),
                () -> assertThat(capturedEvents).anySatisfy(event ->
                        assertThat(event.getEventDetails()).contains(
                                "센서 [sensor-001]의 [온도]데이터에 대한 MAX 룰 위반: 데이터값 41.00은(는) 40.00을(를) 초과하였으므로 알림전송.")
                ),
                () -> {
                    for (ViolatedRuleEventDTO event : capturedEvents) {
                        assertThat(event.getEventLevel()).isEqualTo(EventLevel.WARN);
                        assertThat(event.getSourceId()).isEqualTo("sensor-001");
                        assertThat(event.getSourceType()).isEqualTo("센서");
                        assertThat(event.getDepartmentId()).isEqualTo("서버실");
                    }
                }
        );
    }

    @Test
    @DisplayName("10개의 실시간 데이터가 순차적으로 들어왔을 때 룰 판단 - 모두 통과일 경우")
    void whenManyDataPass() throws Exception{
        List<DataDTO> testDataList = new ArrayList<>();
        for (int i=0; i <= 9; i++) {
            DataDTO data = new DataDTO(
                    gatewayId,
                    sensorId1,
                    dataTypeEnName,
                    20.00 + i,
                    20250520L
            );
            testDataList.add(data);
        }

        when(gatewayAdapter.getDepartmentIdByGatewayId(gatewayId)).thenReturn("서버실");

        for (DataDTO data : testDataList) {
            mockMvc.perform(post("/rule_engine/data")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(data)))
                    .andExpect(status().isOk());
        }

        verify(eventProducer, never()).sendEvent(any());
    }

    @Test
    @DisplayName("10개의 실시간 데이터가 순차적으로 들어왔을 때 룰 판단 - MAX, AVG 룰 위반일 경우")
    void whenManyDataViolatesMaxAndAvgRules_thenSendTwoEvents() throws Exception{
        List<DataDTO> testDataList = new ArrayList<>();
        for (int i=0; i <= 9; i++) {
            DataDTO data = new DataDTO(
                    gatewayId,
                    sensorId1,
                    dataTypeEnName,
                    41.00 + i,
                    20250520L
            );
            testDataList.add(data);
        }

        when(gatewayAdapter.getDepartmentIdByGatewayId(gatewayId)).thenReturn("서버실");

        for (DataDTO data : testDataList) {
            mockMvc.perform(post("/rule_engine/data")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(data)))
                    .andExpect(status().isOk());
        }

        verify(eventProducer, times(20)).sendEvent(any());
    }

    @Test
    @DisplayName("210개 이상의 데이터가 들어올 때 룰 판단 - 모두 통과일 경우")
    void whenTwoHundredDataPass() throws Exception{
        List<DataDTO> testDataList = new ArrayList<>();
        for (int i=0; i <= 9; i++) {
            DataDTO data = new DataDTO(
                    gatewayId,
                    sensorId1,
                    dataTypeEnName,
                    20.00 + i,
                    20250520L
            );
            testDataList.add(data);
        }

        when(gatewayAdapter.getDepartmentIdByGatewayId(gatewayId)).thenReturn("서버실");
        for (int i=0; i<21; i++) {
            for (DataDTO data : testDataList) {
                mockMvc.perform(post("/rule_engine/data")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(data)))
                        .andExpect(status().isOk());
            }
        }

        verify(eventProducer, never()).sendEvent(any());
    }

    @Test
    @DisplayName("210개의 데이터가 들어올 때 룰 판단 - MAX, AVG 룰 위반일 경우")
    void whenTwoHundredDataViolatesMaxAndAvgRules_thenSendTwoEvents() throws Exception{
        List<DataDTO> testDataList = new ArrayList<>();
        for (int i=0; i <= 9; i++) {
            DataDTO data = new DataDTO(
                    gatewayId,
                    sensorId1,
                    dataTypeEnName,
                    41.00 + i,
                    20250520L
            );
            testDataList.add(data);
        }

        when(gatewayAdapter.getDepartmentIdByGatewayId(gatewayId)).thenReturn("서버실");

        for (int i=0; i<21; i++) {
            for (DataDTO data : testDataList) {
                mockMvc.perform(post("/rule_engine/data")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(data)))
                        .andExpect(status().isOk());
            }
        }

        verify(eventProducer, times(420)).sendEvent(any());
    }

    @Test
    @DisplayName("서로 다른 센서의 데이터가 220개 들어왔을 경우 - 모두 통과")
    void whenEachOtherTwoHundredDataPass() throws Exception{
        List<DataDTO> testDataList = new ArrayList<>();
        for (int i=0; i <= 9; i++) {
            DataDTO data1 = new DataDTO(
                    gatewayId,
                    sensorId1,
                    dataTypeEnName,
                    20.00 + i,
                    20250520L
            );
            DataDTO data2 = new DataDTO(
                    gatewayId,
                    sensorId2,
                    dataTypeEnName,
                    20.00 + i,
                    20250520L
            );
            testDataList.add(data1);
            testDataList.add(data2);
        }



        when(gatewayAdapter.getDepartmentIdByGatewayId(gatewayId)).thenReturn("서버실");
        for (int i=0; i<11; i++) {
            for (DataDTO data : testDataList) {
                mockMvc.perform(post("/rule_engine/data")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(data)))
                        .andExpect(status().isOk());
            }
        }

        verify(eventProducer, never()).sendEvent(any());
    }

    @Test
    @DisplayName("서로 다른 센서의 데이터가 220개 들어왔을 경우 - 모든 룰 위반일 경우")
    void whenEachOtherTwoHundredDataViolatesMaxAndAvgRules_thenSendTwoEvents() throws Exception {
        List<DataDTO> testDataList = new ArrayList<>();
        for (int i=0; i <= 9; i++) {
            DataDTO data1 = new DataDTO(
                    gatewayId,
                    sensorId1,
                    dataTypeEnName,
                    41.00 + i,
                    20250520L
            );
            DataDTO data2 = new DataDTO(
                    gatewayId,
                    sensorId2,
                    dataTypeEnName,
                    0.00 + i,
                    20250520L
            );
            testDataList.add(data1);
            testDataList.add(data2);
        }

        when(gatewayAdapter.getDepartmentIdByGatewayId(gatewayId)).thenReturn("서버실");

        for (int i=0; i<11; i++) {
            for (DataDTO data : testDataList) {
                mockMvc.perform(post("/rule_engine/data")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(data)))
                        .andExpect(status().isOk());
            }
        }

        verify(eventProducer, times(440)).sendEvent(any());
    }

    @Test
    @DisplayName("비정상 요청에 대한 테스트")
    void whenInvalidData_thenReturnBadRequest() throws Exception {
        mockMvc.perform(post("/rule_engine/data")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}")) // 비어 있음
                .andExpect(status().isBadRequest());
    }

}
