package com.nhnacademy.ruleengineservice.sensor_data.service.impl;

import com.nhnacademy.ruleengineservice.enums.ActionType;
import com.nhnacademy.ruleengineservice.enums.EventLevel;
import com.nhnacademy.ruleengineservice.enums.Operator;
import com.nhnacademy.ruleengineservice.enums.RuleType;
import com.nhnacademy.ruleengineservice.gateway.adapter.GatewayAdapter;
import com.nhnacademy.ruleengineservice.message.dto.ViolatedRuleMessageDTO;
import com.nhnacademy.ruleengineservice.message.adapter.MessageAdapter;
import com.nhnacademy.ruleengineservice.sensor_data.dto.DataDTO;
import com.nhnacademy.ruleengineservice.sensor_rule.domain.SensorRule;
import com.nhnacademy.ruleengineservice.sensor_rule.service.SensorRuleViolationService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SensorDataProcessorServiceImplTest {

    @Mock
    private SensorRuleViolationService violationChecker;

    @Mock
    private MessageAdapter messageAdapter;

    @Mock
    private GatewayAdapter gatewayAdapter;

    @InjectMocks
    private SensorDataProcessorServiceImpl processorService;

    private static DataDTO dataDTO;

    @BeforeEach
    void setUp() {
        dataDTO = new DataDTO(
                "gateway1",
                "sensor1",
                "temperature",
                40.0,
                20250508L
        );
    }

    @Test
    @DisplayName("위반한 룰이 없는 경우")
    void processTest() {
        when(violationChecker.getViolatedRules(any(DataDTO.class))).thenReturn(Collections.emptyList());

        processorService.process(dataDTO);

        verify(messageAdapter, never()).send(any());
    }

    @Test
    @DisplayName("위반한 룰이 있는 경우")
    void processValidTest() {
        SensorRule sensorRule1 = SensorRule.createRule(
                "gateway1",
                "sensor1",
                "temperature",
                RuleType.MIN,
                Operator.LESS_THAN,
                50.0,
                5.0,
                ActionType.SEND_ALERT
        );

        when(violationChecker.getViolatedRules(any(DataDTO.class))).thenReturn(List.of(sensorRule1));
        when(gatewayAdapter.getDepartmentIdByGatewayId(any(String.class))).thenReturn("department1");

        processorService.process(dataDTO);

        ArgumentCaptor<ViolatedRuleMessageDTO> captor = ArgumentCaptor.forClass(ViolatedRuleMessageDTO.class);
        verify(messageAdapter, times(1)).send(captor.capture());

        ViolatedRuleMessageDTO sentMessage = captor.getValue();

        Assertions.assertAll(
                ()->{
                    assertEquals(EventLevel.WARN, sentMessage.getEventLevel());
                    assertEquals("센서 sensor1의 MIN 룰 위반: 데이터값 40.00은(는) 50.00미만이므로 알림전송.", sentMessage.getEventDetails());
                    assertEquals("sensor1", sentMessage.getSourceId());
                    assertEquals("센서", sentMessage.getSourceType());
                    assertEquals("department1", sentMessage.getDepartmentId());
                    assertNotNull(sentMessage.getEventAt());
                }
        );
    }
}