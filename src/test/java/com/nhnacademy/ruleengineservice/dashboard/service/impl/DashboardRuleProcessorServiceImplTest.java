package com.nhnacademy.ruleengineservice.dashboard.service.impl;

import com.nhnacademy.ruleengineservice.common.exception.SensorRuleException;
import com.nhnacademy.ruleengineservice.dashboard.dto.RuleCreateRequest;
import com.nhnacademy.ruleengineservice.dashboard.dto.RuleDeleteRequest;
import com.nhnacademy.ruleengineservice.sensor_rule.service.SensorRuleDeleteService;
import com.nhnacademy.ruleengineservice.sensor_rule.service.SensorRuleGenerateService;
import com.nhnacademy.ruleengineservice.sensor_rule.service.SensorRuleService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DashboardRuleProcessorServiceImplTest {
    @Mock
    private SensorRuleGenerateService sensorRuleGenerateService;

    @Mock
    private SensorRuleDeleteService sensorRuleDeleteService;

    @InjectMocks
    private DashboardRuleProcessorServiceImpl dashboardRuleProcessorService;

    private RuleCreateRequest createRequest;
    private RuleDeleteRequest deleteRequest;

    @BeforeEach
    void setUp() {
        createRequest = new RuleCreateRequest();

        createRequest.setGatewayId(1L);
        createRequest.setSensorId("sensor1");
        createRequest.setDepartmentId("부서1");
        createRequest.setDataTypeEnName("temperature");
        createRequest.setDataTypeKrName("온도");
        createRequest.setThresholdMin(10.00);
        createRequest.setThresholdMax(40.00);

        deleteRequest = new RuleDeleteRequest();
        deleteRequest.setGatewayId(1L);
        deleteRequest.setSensorId("sensor1");
        deleteRequest.setDepartmentId("부서1");
        deleteRequest.setDataTypeEnName("temperature");
        deleteRequest.setDataTypeKrName("온도");
    }

    @Test
    @DisplayName("generateRulesFromCreateDto 정상 수행")
    void generateRulesFromCreateDto_success() {
        // given + when
        dashboardRuleProcessorService.generateRulesFromCreateDto(createRequest);

        // then
        verify(sensorRuleGenerateService, times(1)).generateRules(createRequest);
    }

    @Test
    @DisplayName("generateRulesFromCreateDto 예외 발생 시 SensorRuleException 다시 throw")
    void generateRulesFromCreateDto_exceptionThrown() {
        // given
        doThrow(new SensorRuleException("룰 생성 실패")).when(sensorRuleGenerateService).generateRules(createRequest);

        // when + then
        SensorRuleException thrown = assertThrows(
                SensorRuleException.class,
                () -> dashboardRuleProcessorService.generateRulesFromCreateDto(createRequest)
        );

        assertEquals("룰 생성 실패", thrown.getMessage());
        verify(sensorRuleGenerateService, times(1)).generateRules(createRequest);
    }

    @Test
    @DisplayName("deleteRulesFromCreateDto 정상 수행")
    void deleteRulesFromDeleteDto_success() {
        // given + when
        dashboardRuleProcessorService.deleteRulesFromDeleteDto(deleteRequest);

        // then
        verify(sensorRuleDeleteService, times(1)).deleteRules(deleteRequest);
    }

    @Test
    @DisplayName("deleteRulesFromDeleteDto 예외 발생 시 SensorRuleException 다시 throw")
    void deleteRulesFromDeleteDto_exceptionThrown() {
        // given
        doThrow(new SensorRuleException("룰 삭제 실패")).when(sensorRuleDeleteService).deleteRules(deleteRequest);

        // when + then
        SensorRuleException thrown = assertThrows(
                SensorRuleException.class,
                () -> dashboardRuleProcessorService.deleteRulesFromDeleteDto(deleteRequest)
        );

        assertEquals("룰 삭제 실패", thrown.getMessage());
        verify(sensorRuleDeleteService, times(1)).deleteRules(deleteRequest);
    }
}