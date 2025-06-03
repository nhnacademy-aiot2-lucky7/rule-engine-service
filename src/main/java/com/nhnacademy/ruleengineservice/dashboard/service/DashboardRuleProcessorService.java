package com.nhnacademy.ruleengineservice.dashboard.service;

import com.nhnacademy.ruleengineservice.dashboard.dto.RuleCreateRequest;

public interface DashboardRuleProcessorService {
    void generateRulesFromCreateDto(RuleCreateRequest request);
}
