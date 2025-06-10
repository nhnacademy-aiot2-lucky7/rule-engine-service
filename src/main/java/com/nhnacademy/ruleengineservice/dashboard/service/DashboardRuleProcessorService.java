package com.nhnacademy.ruleengineservice.dashboard.service;

import com.nhnacademy.ruleengineservice.dashboard.dto.RuleCreateRequest;
import com.nhnacademy.ruleengineservice.dashboard.dto.RuleDeleteRequest;

public interface DashboardRuleProcessorService {
    void generateRulesFromCreateDto(RuleCreateRequest request);
    void deleteRulesFromDeleteDto(RuleDeleteRequest request);
}
