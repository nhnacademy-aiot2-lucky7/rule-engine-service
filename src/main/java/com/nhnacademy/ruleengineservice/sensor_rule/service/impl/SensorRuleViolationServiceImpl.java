package com.nhnacademy.ruleengineservice.sensor_rule.service.impl;

import com.nhnacademy.ruleengineservice.enums.Operator;
import com.nhnacademy.ruleengineservice.enums.RuleType;
import com.nhnacademy.ruleengineservice.sensor_data.dto.DataDTO;
import com.nhnacademy.ruleengineservice.sensor_rule.domain.SensorRule;
import com.nhnacademy.ruleengineservice.sensor_rule.service.SensorRuleService;
import com.nhnacademy.ruleengineservice.sensor_rule.service.SensorRuleViolationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class SensorRuleViolationServiceImpl implements SensorRuleViolationService {
    private final SensorRuleService sensorRuleService;

    public List<SensorRule> getViolatedRules(DataDTO dataDTO) {
        List<SensorRule> violatedRules = new ArrayList<>();
        List<RuleType> ruleTypes = List.of(RuleType.MAX, RuleType.MIN, RuleType.AVG);

        for (RuleType ruleType : ruleTypes) {
            SensorRule rule = sensorRuleService.getSensorRule(
                    dataDTO.getGatewayId(),
                    dataDTO.getSensorId(),
                    dataDTO.getDataType(),
                    ruleType
            );

            if (rule != null) {
                Operator operator = rule.getOperator();
                double targetvalue = rule.getValue();
                double sensorValue = dataDTO.getValue();
                boolean isValid = operator.compare(dataDTO.getValue(), targetvalue);

                log.debug("▶ Rule 위반검사 -  타입: {}, 연산자: {}, 기준값: {}, 센서값: {} → 결과: {}",
                        ruleType,
                        operator.getSymbol(),
                        String.format("%.2f", targetvalue),
                        String.format("%.2f", sensorValue),
                        isValid ? "위반":"통과"
                );

                if (isValid) {
                    violatedRules.add(rule);
                }

            } else {
                log.warn("룰 없음 - 게이트웨이: {}, 센서: {}, 데이터타입: {}, 타입: {}",
                        dataDTO.getGatewayId(), dataDTO.getSensorId(), dataDTO.getDataType(), ruleType);
            }
        }
        return violatedRules;
    }
}
