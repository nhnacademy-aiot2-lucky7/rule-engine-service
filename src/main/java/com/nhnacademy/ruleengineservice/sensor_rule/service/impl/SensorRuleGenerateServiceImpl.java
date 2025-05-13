package com.nhnacademy.ruleengineservice.sensor_rule.service.impl;

import com.nhnacademy.ruleengineservice.enums.ActionType;
import com.nhnacademy.ruleengineservice.enums.Operator;
import com.nhnacademy.ruleengineservice.enums.RuleType;
import com.nhnacademy.ruleengineservice.sensor_rule.domain.SensorRule;
import com.nhnacademy.ruleengineservice.sensor_rule.service.RuleGenerationStrategy;
import com.nhnacademy.ruleengineservice.sensor_rule.service.SensorRuleGenerateService;
import com.nhnacademy.ruleengineservice.sensor_rule.service.SensorRuleService;
import com.nhnacademy.ruleengineservice.threshold.dto.ThresholdAnalysisDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class SensorRuleGenerateServiceImpl implements SensorRuleGenerateService {

    private final SensorRuleService sensorRuleService;

    @Override
    public void generateRules(List<ThresholdAnalysisDTO> analysisDTOList) {
        for (ThresholdAnalysisDTO dto : analysisDTOList) {
            log.info("Generating rules for gatewayId: {}, sensorId: {}, dataType: {}", dto.getGatewayId(), dto.getSensorId(), dto.getDataType());
            ruleGenerators.values().forEach(strategy -> strategy.generate(dto, sensorRuleService));
        }
    }

    private final Map<RuleType, RuleGenerationStrategy> ruleGenerators = Map.of(
            RuleType.MIN, (dto, service) -> {
                if (dto.getThresholdMin() != null) {
                    SensorRule rule = SensorRule.createRule(
                            dto.getGatewayId(),
                            dto.getSensorId(),
                            dto.getDataType(),
                            RuleType.MIN,
                            Operator.LESS_THAN,
                            dto.getThresholdMin(),
                            null,
                            null,
                            ActionType.SEND_ALERT
                    );
                    log.info("SensorRule: {}", rule);
                    service.saveSensorRule(rule);
                }
            },
            RuleType.MAX, (dto, service) -> {
                if (dto.getThresholdMax() != null) {
                    SensorRule rule = SensorRule.createRule(
                            dto.getGatewayId(),
                            dto.getSensorId(),
                            dto.getDataType(),
                            RuleType.MAX,
                            Operator.GREATER_THAN,
                            dto.getThresholdMax(),
                            null,
                            null,
                            ActionType.SEND_ALERT
                    );
                    log.info("SensorRule: {}", rule);
                    service.saveSensorRule(rule);
                }
            },
            RuleType.AVG, (dto, service) -> {
                if (dto.getThresholdAvgMin() != null && dto.getThresholdAvgMax() != null) {
                    SensorRule rule = SensorRule.createRule(
                            dto.getGatewayId(),
                            dto.getSensorId(),
                            dto.getDataType(),
                            RuleType.AVG,
                            Operator.OUT_OF_BOUND,
                            dto.getThresholdAvg(),
                            dto.getThresholdAvgMin(),
                            dto.getThresholdAvgMax(),
                            ActionType.SEND_ALERT
                    );
                    log.info("SensorRule: {}", rule);
                    service.saveSensorRule(rule);
                }
            }
    );
}
