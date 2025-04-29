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
public class SensorRuleFilterChain {

    private final SensorRuleService sensorRuleService;

    public boolean filter(Map<String, Object> sensorData) {
        return matchAllRules(sensorData);
    }

    // 주어진 sensorData가 rule에 맞는지 체크
    public boolean matchAllRules(Map<String, Object> sensorData) {
        String deviceId = (String) sensorData.get("deviceId");
        String dataType = (String) sensorData.get("datatype");

        Double sensorValue = (Double) sensorData.get("value");

        /*double sensorValue;
        // 1. dataValue를 double로 변환
        if (dataValue instanceof Number) {
            sensorValue = ((Number) dataValue).doubleValue();
        } else if (dataValue instanceof String) {
            try {
                sensorValue = Double.parseDouble((String) dataValue);
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("숫자 형식이 잘못된 문자열입니다: " + dataValue);
            }
        } else {
            throw new IllegalArgumentException("지원하지 않는 데이터 타입입니다: " + dataValue.getClass());
        }*/

        // 해당 deviceId + dataType에 대한 룰들 조회
        List<Rule> ruleList = sensorRuleService.getRulesByKey(deviceId, dataType);

        for (Rule rule : ruleList) {
            double targetValue = rule.getValue(); // 이미 해당 ruletype의 값만 있으니 바로 사용
            Operator operator = rule.getOperator();

            boolean passed = operator.compare(sensorValue, targetValue);

            System.out.printf("▶ Rule 검사중 - 타입: %s, 연산자: %s, 기준값: %.2f, 센서값: %.2f → 결과: %s%n",
                    rule.getRuletype(), operator.getSymbol(), targetValue, sensorValue, passed ? "통과" : "불통");

            if (!passed) {
                return false; // 하나라도 실패하면 전체 실패
            }
        }
        return true; // 모든 룰을 통과했으면 true
    }
}
