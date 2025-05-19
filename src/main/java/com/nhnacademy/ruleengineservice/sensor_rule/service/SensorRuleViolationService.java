package com.nhnacademy.ruleengineservice.sensor_rule.service;

import com.nhnacademy.ruleengineservice.sensor_data.dto.DataDTO;
import com.nhnacademy.ruleengineservice.sensor_rule.domain.SensorRule;

import java.util.List;

public interface SensorRuleViolationService {
    List<SensorRule> getViolatedRules(DataDTO dataDTO);
}
