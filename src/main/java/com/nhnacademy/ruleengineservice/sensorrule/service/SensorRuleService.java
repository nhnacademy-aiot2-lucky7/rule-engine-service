package com.nhnacademy.ruleengineservice.sensorrule.service;

import com.nhnacademy.ruleengineservice.sensorrule.domain.Rule;

import java.util.List;

public interface SensorRuleService {
    void saveSensorRules(String deviceId, String dataType, Rule rule);
    List<Rule> getRulesByKey(String deviceId, String dataType);
}
