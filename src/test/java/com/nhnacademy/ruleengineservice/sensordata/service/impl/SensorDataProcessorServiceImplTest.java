package com.nhnacademy.ruleengineservice.sensordata.service.impl;

import com.nhnacademy.ruleengineservice.common.filter.SensorRuleViolationChecker;
import com.nhnacademy.ruleengineservice.message.service.MessageService;
import com.nhnacademy.ruleengineservice.sensordata.service.SensorDataProcessorService;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.*;

class SensorDataProcessorServiceImplTest {

    @InjectMocks
    private SensorDataProcessorService processorService;

    @Mock
    private SensorRuleViolationChecker violationChecker;

    @Mock
    private MessageService messageService;



}