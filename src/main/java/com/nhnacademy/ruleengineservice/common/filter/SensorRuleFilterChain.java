package com.nhnacademy.ruleengineservice.common.filter;

import com.nhnacademy.ruleengineservice.sensorrule.domain.Rule;
import com.nhnacademy.ruleengineservice.sensorrule.domain.SensorRule;
import com.nhnacademy.ruleengineservice.sensorrule.service.SensorRuleService;
import lombok.AllArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

@AllArgsConstructor
public class SensorRuleFilterChain {

    private final List<Predicate<Map<String, Object>>> filters = new ArrayList<>();
    private final SensorRuleService sensorRuleService;

    public SensorRuleFilterChain addFilter(SensorRule rule) {
        filters.add(sensorData -> matchRule(rule, sensorData));
        return this;
    }

    public boolean filter(Map<String, Object> sensorData) {
        return filters.stream()
                .allMatch(predicate -> predicate.test(sensorData));
    }

    // 주어진 sensorData가 rule에 맞는지 체크
    private boolean matchRule(Rule rule, Map<String, Object> sensorData) {
        // sensorData에서 필드 추출 (예: "datatype", "value" 등)
        String deviceId = (String) sensorData.get("deviceId");
        String dataType = (String) sensorData.get("datatype");
        Object value = sensorData.get("value");

        List<Rule> ruleList = sensorRuleService.getRulesByKey(deviceId, dataType);



        // 데이터 타입과 값이 룰과 일치하는지 확인
        if (dataType != null && dataType.equals(rule.getDatatype().toString())) {
            if (rule.getOperator().equals("equals") && value != null && value.equals(rule.getValue())) {
                return true;
            } else if (rule.getOperator().equals("range") && value != null && value instanceof Double) {
                Double doubleValue = (Double) value;
                if (doubleValue >= (Double) rule.getValue() && doubleValue <= (Double) range) {
                    return true;
                }
            }
        }

        // 필터링 조건에 맞지 않으면 false
        return false;
    }
}
