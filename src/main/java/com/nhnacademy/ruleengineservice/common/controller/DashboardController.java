package com.nhnacademy.ruleengineservice.common.controller;

import com.nhnacademy.ruleengineservice.dashboard.dto.RuleCreateRequest;
import com.nhnacademy.ruleengineservice.dashboard.dto.RuleDeleteRequest;
import com.nhnacademy.ruleengineservice.dashboard.service.DashboardRuleProcessorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/rule_engine/rules")
public class DashboardController {
    private final DashboardRuleProcessorService dashboardRuleProcessorService;

    @PostMapping("/create_rule")
    public ResponseEntity<Void> createRule(@RequestBody RuleCreateRequest request) {
        log.info("룰 생성을 위한 데이터 받기 성공!");
        log.info(String.format("받은 데이터: %s, %s, %s, %s, %s, %s, %s",
                request.getGatewayId(),
                request.getSensorId(),
                request.getDepartmentId(),
                request.getDataTypeEnName(),
                request.getDataTypeKrName(),
                request.getThresholdMin(),
                request.getThresholdMax()));
        dashboardRuleProcessorService.generateRulesFromCreateDto(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping("/delete_rule")
    public ResponseEntity<Void> deleteRule(@RequestBody RuleDeleteRequest request) {
        log.info("룰 삭제을 위한 정보 받기 성공!");
        dashboardRuleProcessorService.deleteRulesFromDeleteDto(request);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
