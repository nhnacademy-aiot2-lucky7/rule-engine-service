package com.nhnacademy.ruleengineservice.common.filter;

import com.nhnacademy.ruleengineservice.enums.Operator;
import com.nhnacademy.ruleengineservice.sensorrule.domain.Rule;
import com.nhnacademy.ruleengineservice.sensorrule.service.SensorRuleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class SensorRuleViolationChecker {

    private final SensorRuleService sensorRuleService;

    /**
     * 센서 데이터에서 위반된 룰 목록 반환
     * @param sensorData - Map에 gatewayId, deviceId, dataType, value 포함
     * @return 위반된 Rule 리스트
     */
    public List<Rule> getViolatedRules(Map<String, Object> sensorData) {
        String gatewayId = (String) sensorData.get("gatewayId");
        String deviceId = (String) sensorData.get("deviceId");
        String dataType = (String) sensorData.get("dataType");
        Double sensorValue = (Double) sensorData.get("value");


        List<Rule> rules = sensorRuleService.getRulesByKey(gatewayId, deviceId, dataType);

        return rules.stream()
                .filter(rule -> {
                    boolean passed = rule.getOperator().compare(sensorValue, rule.getValue());
                    System.out.printf("Rule 위반검사 - [%s] %s %.2f vs %.2f → %s%n",
                            rule.getRuletype(), rule.getOperator().getSymbol(), sensorValue, rule.getValue(),
                            passed ? "정상" : "위반");
                    return !passed;
                })
                .toList();
    }
}
