package com.nhnacademy.ruleengineservice.common.filter;

import com.nhnacademy.ruleengineservice.enums.Operator;
import com.nhnacademy.ruleengineservice.enums.RuleType;
import com.nhnacademy.ruleengineservice.sensorrule.domain.Rule;
import com.nhnacademy.ruleengineservice.sensorrule.service.SensorRuleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class SensorRuleViolationChecker {

    private final SensorRuleService sensorRuleService;

    /**
     * 센서 데이터에서 위반된 룰 목록 반환
     * @param sensorData - Map에 gatewayId, sensorId, dataType, value 포함
     * @return 위반된 Rule 리스트
     */
    public List<Rule> getViolatedRules(Map<String, Object> sensorData) {
        String gatewayId = (String) sensorData.get("gatewayId");
        String sensorId = (String) sensorData.get("sensorId");
        String dataType = (String) sensorData.get("dataType");
        Double sensorValue = (Double) sensorData.get("value");


        List<Rule> rules = sensorRuleService.getRulesByKey(gatewayId, sensorId, dataType);

        return rules.stream()
                .filter(rule -> {
                    RuleType ruleType = rule.getRuletype();
                    Operator operator = rule.getOperator();
                    double targetValue = rule.getValue();
                    boolean passed = operator.compare(sensorValue, targetValue);
                    log.info(String.format("▶ Rule 위반검사 - 타입: %s, 연산자: %s, 기준값: %.2f, 센서값: %.2f → 결과: %s%n",
                            ruleType,
                            operator.getSymbol(),
                            targetValue,
                            sensorValue,
                            !passed ? "통과" : "위반"));
                    return passed;
                })
                .toList();
    }
}
