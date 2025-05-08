package com.nhnacademy.ruleengineservice.sensor_rule.service;

import com.nhnacademy.ruleengineservice.common.exception.SensorRuleNotFoundException;
import com.nhnacademy.ruleengineservice.enums.Operator;
import com.nhnacademy.ruleengineservice.enums.RuleType;
import com.nhnacademy.ruleengineservice.sensor_data.dto.DataDTO;
import com.nhnacademy.ruleengineservice.sensor_rule.domain.SensorRule;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class SensorRuleViolationService {

    private final SensorRuleService sensorRuleService;

    public List<SensorRule> getViolatedRules(DataDTO dataDTO) {
        List<SensorRule> violatedRules = new ArrayList<>();

        for (RuleType ruleType : List.of(RuleType.MAX, RuleType.MIN, RuleType.AVG)) {
            try {
                SensorRule rule = sensorRuleService.getSensorRule(
                        dataDTO.getGatewayId(),
                        dataDTO.getSensorId(),
                        dataDTO.getDataType(),
                        ruleType
                );

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

            } catch (Exception e) {
                throw new SensorRuleNotFoundException(dataDTO.getSensorId(), dataDTO.getDataType());
            }
        }
        return violatedRules;
    }
}
