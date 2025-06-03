package com.nhnacademy.ruleengineservice.sensor_rule.service.impl;

import com.nhnacademy.ruleengineservice.common.exception.SensorRuleCreationException;
import com.nhnacademy.ruleengineservice.dashboard.dto.RuleCreateRequest;
import com.nhnacademy.ruleengineservice.enums.*;
import com.nhnacademy.ruleengineservice.event.dto.ViolatedRuleEventDTO;
import com.nhnacademy.ruleengineservice.event.producer.EventProducer;
import com.nhnacademy.ruleengineservice.sensor_rule.domain.SensorRule;
import com.nhnacademy.ruleengineservice.sensor_rule.service.RuleGenerationStrategy;
import com.nhnacademy.ruleengineservice.sensor_rule.service.SensorRuleGenerateService;
import com.nhnacademy.ruleengineservice.sensor_rule.service.SensorRuleService;
import io.micrometer.common.util.StringUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class SensorRuleGenerateServiceImpl implements SensorRuleGenerateService {

    private static final String EVENT_DETAIL_TEMPLATE = "센서 [%s]의 [%s] 데이터에 대한 룰이 %s되었습니다.";
    private static final String SENSOR_RULE_CREATION_MISSING_REQUIRED = "SensorRule 생성 실패: 필수값이 누락되었습니다.";
    private static final String SOURCE_TYPE = "센서";

    private final SensorRuleService sensorRuleService;
    private final EventProducer eventProducer;

    /**
     * 전략별 Rule 생성기 맵
     */
    private final Map<RuleType, RuleGenerationStrategy> ruleGenerators = Map.of(
            RuleType.MIN, dto -> {
                if (dto.getThresholdMin() == null) return null;

                return SensorRule.builder()
                        .gatewayId(dto.getGatewayId())
                        .sensorId(dto.getSensorId())
                        .departmentId(dto.getDepartmentId())
                        .dataTypeEnName(dto.getDataTypeEnName())
                        .dataTypeKrName(dto.getDataTypeKrName())
                        .ruleType(RuleType.MIN)
                        .operator(Operator.LESS_THAN)
                        .value(dto.getThresholdMin())
                        .action(ActionType.SEND_ALERT)
                        .build();
            },

            RuleType.MAX, dto -> {
                if (dto.getThresholdMax() == null) return null;

                return SensorRule.builder()
                        .gatewayId(dto.getGatewayId())
                        .sensorId(dto.getSensorId())
                        .departmentId(dto.getDepartmentId())
                        .dataTypeEnName(dto.getDataTypeEnName())
                        .dataTypeKrName(dto.getDataTypeKrName())
                        .ruleType(RuleType.MAX)
                        .operator(Operator.GREATER_THAN)
                        .value(dto.getThresholdMax())
                        .action(ActionType.SEND_ALERT)
                        .build();
            }
    );

    @Override
    public void generateRules(RuleCreateRequest createRequest) {
        validateRequiredFields(createRequest);

        List<SensorRule> generatedRules = new ArrayList<>();
        boolean anyUpdated = false;

        for (RuleGenerationStrategy strategy : ruleGenerators.values()) {
            SensorRule rule = strategy.generate(createRequest);
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
            sendRuleGeneratedEvent(createRequest, overallStatus);
        }
    }

    private void validateRequiredFields(RuleCreateRequest request) {
        if (StringUtils.isBlank(request.getGatewayId().toString()) ||
                StringUtils.isBlank(request.getSensorId()) ||
                StringUtils.isBlank(request.getDepartmentId()) ||
                StringUtils.isBlank(request.getDataTypeEnName()) ||
                StringUtils.isBlank(request.getDataTypeKrName())) {
            throw new SensorRuleCreationException(SENSOR_RULE_CREATION_MISSING_REQUIRED);
        }
    }

    private void sendRuleGeneratedEvent(RuleCreateRequest createRequest, SaveStatus status) {
        String eventDetail = String.format(EVENT_DETAIL_TEMPLATE,
                    createRequest.getSensorId(),
                    createRequest.getDataTypeKrName(),
                    status.getDesc()
        );

        ViolatedRuleEventDTO eventDTO = new ViolatedRuleEventDTO(
                EventLevel.INFO,
                eventDetail,
                createRequest.getSensorId(),
                SOURCE_TYPE,
                createRequest.getDepartmentId(),
                LocalDateTime.now()
        );

        eventProducer.sendEvent(eventDTO);
        log.info("Event message: {}", eventDetail);
    }
}
