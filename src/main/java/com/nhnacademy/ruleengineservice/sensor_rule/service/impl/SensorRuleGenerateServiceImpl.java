package com.nhnacademy.ruleengineservice.sensor_rule.service.impl;

import com.nhnacademy.ruleengineservice.common.exception.SensorRuleCreationException;
import com.nhnacademy.ruleengineservice.enums.*;
import com.nhnacademy.ruleengineservice.event.dto.ViolatedRuleEventDTO;
import com.nhnacademy.ruleengineservice.event.producer.EventProducer;
import com.nhnacademy.ruleengineservice.gateway.adapter.GatewayAdapter;
import com.nhnacademy.ruleengineservice.sensor_rule.domain.SensorRule;
import com.nhnacademy.ruleengineservice.sensor_rule.service.RuleGenerationStrategy;
import com.nhnacademy.ruleengineservice.sensor_rule.service.SensorRuleGenerateService;
import com.nhnacademy.ruleengineservice.sensor_rule.service.SensorRuleService;
import com.nhnacademy.ruleengineservice.threshold.dto.ThresholdAnalysisDTO;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class SensorRuleGenerateServiceImpl implements SensorRuleGenerateService {

    private static final String EVENT_DETAIL_TEMPLATE = "센서 [%s]의 [%s] 데이터에 대한 룰이 %s되었습니다.";
    private static final String SENSOR_RULE_CREATION_MISSING_REQUIRED = "SensorRule 생성 실패: 필수값이 누락되었습니다.";
    private static final String SOURCE_TYPE = "센서";

    private final SensorRuleService sensorRuleService;
    private final GatewayAdapter gatewayAdapter;
    private final EventProducer eventProducer;

    /**
     * 전략별 Rule 생성기 맵
     */
    private final Map<RuleType, RuleGenerationStrategy> ruleGenerators = Map.of(
            RuleType.MIN, dto -> {
                if (dto.getThresholdMin() != null) {
                    try {
                        return SensorRule.builder()
                                .gatewayId(dto.getGatewayId())
                                .sensorId(dto.getSensorId())
                                .dataTypeEnName(dto.getDataTypeEnName())
                                .dataTypeKrName(dto.getDataTypeKrName())
                                .ruleType(RuleType.MIN)
                                .operator(Operator.LESS_THAN)
                                .value(dto.getThresholdMin())
                                .action(ActionType.SEND_ALERT)
                                .build();
                    } catch (NullPointerException e) {
                        throw new SensorRuleCreationException(SENSOR_RULE_CREATION_MISSING_REQUIRED);
                    }
                }
                return null;
            },
            RuleType.MAX, dto -> {
                if (dto.getThresholdMax() != null) {
                    try {
                        return SensorRule.builder()
                                .gatewayId(dto.getGatewayId())
                                .sensorId(dto.getSensorId())
                                .dataTypeEnName(dto.getDataTypeEnName())
                                .dataTypeKrName(dto.getDataTypeKrName())
                                .ruleType(RuleType.MAX)
                                .operator(Operator.GREATER_THAN)
                                .value(dto.getThresholdMax())
                                .action(ActionType.SEND_ALERT)
                                .build();
                    } catch (NullPointerException e) {
                        throw new SensorRuleCreationException(SENSOR_RULE_CREATION_MISSING_REQUIRED);
                    }
                }
                return null;
            },
            RuleType.AVG, dto -> {
                if (dto.getThresholdAvgMin() != null && dto.getThresholdAvgMax() != null) {
                    try {
                        return SensorRule.builder()
                                .gatewayId(dto.getGatewayId())
                                .sensorId(dto.getSensorId())
                                .dataTypeEnName(dto.getDataTypeEnName())
                                .dataTypeKrName(dto.getDataTypeKrName())
                                .ruleType(RuleType.AVG)
                                .operator(Operator.OUT_OF_BOUND)
                                .value(dto.getThresholdAvg())
                                .minValue(dto.getThresholdAvgMin())
                                .maxValue(dto.getThresholdAvgMax())
                                .action(ActionType.SEND_ALERT)
                                .build();
                    } catch (NullPointerException e) {
                        throw new SensorRuleCreationException(SENSOR_RULE_CREATION_MISSING_REQUIRED);
                    }
                }
                return null;
            }
    );

    @Override
    public void generateRules(List<ThresholdAnalysisDTO> analysisDTOList) {
        for (ThresholdAnalysisDTO analysisDTO : analysisDTOList) {
            generateRulesForOneSensor(analysisDTO);
        }
    }

    private void generateRulesForOneSensor(ThresholdAnalysisDTO analysisDTO) {
        List<SensorRule> generatedRules = new ArrayList<>();
        boolean anyUpdated = false;

        for (RuleGenerationStrategy strategy : ruleGenerators.values()) {
            SensorRule rule = strategy.generate(analysisDTO);
            if (rule != null) {
                SaveStatus status = sensorRuleService.saveSensorRule(rule);
                if (status == SaveStatus.UPDATED) {
                    anyUpdated = true;
                }
                generatedRules.add(rule);
            }
        }

        if (!generatedRules.isEmpty()) {
            SaveStatus overallStatus = anyUpdated ? SaveStatus.UPDATED : SaveStatus.NEW;
            sendRuleGeneratedEvent(analysisDTO, overallStatus);
        }
    }

    private void sendRuleGeneratedEvent(ThresholdAnalysisDTO dataDTO, SaveStatus status) {
        String eventDetail = String.format(EVENT_DETAIL_TEMPLATE,
                    dataDTO.getSensorId(),
                    dataDTO.getDataTypeKrName(),
                    status.getDesc()
        );

        ViolatedRuleEventDTO eventDTO = new ViolatedRuleEventDTO(
                EventLevel.INFO,
                eventDetail,
                dataDTO.getSensorId(),
                SOURCE_TYPE,
                gatewayAdapter.getDepartmentIdByGatewayId(dataDTO.getGatewayId()),
                LocalDateTime.now()
        );

        eventProducer.sendEvent(eventDTO);
        log.info("Event message: {}", eventDetail);
    }
}
