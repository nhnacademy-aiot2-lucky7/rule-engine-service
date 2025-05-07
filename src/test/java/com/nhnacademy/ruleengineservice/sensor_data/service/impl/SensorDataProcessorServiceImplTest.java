package com.nhnacademy.ruleengineservice.sensor_data.service.impl;

import com.nhnacademy.ruleengineservice.sensor_rule.service.SensorRuleViolationService;
import com.nhnacademy.ruleengineservice.message.service.MessageService;
import com.nhnacademy.ruleengineservice.sensor_data.service.SensorDataProcessorService;
import org.mockito.InjectMocks;
import org.mockito.Mock;

class SensorDataProcessorServiceImplTest {

    @InjectMocks
    private SensorDataProcessorService processorService;

    @Mock
    private SensorRuleViolationService violationChecker;

    @Mock
    private MessageService messageService;



}