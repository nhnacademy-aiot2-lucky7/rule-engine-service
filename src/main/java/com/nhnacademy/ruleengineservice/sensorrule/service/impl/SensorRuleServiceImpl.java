package com.nhnacademy.ruleengineservice.sensorrule.service.impl;

import com.nhnacademy.ruleengineservice.common.exception.SensorRuleNotFoundException;
import com.nhnacademy.ruleengineservice.sensorrule.domain.Rule;
import com.nhnacademy.ruleengineservice.sensorrule.domain.SensorRule;
import com.nhnacademy.ruleengineservice.sensorrule.service.SensorRuleService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SensorRuleServiceImpl implements SensorRuleService {

    private final RedisTemplate<String, SensorRule> redisTemplate;

    public void saveSensorRules(String gatewayId, String sensorId, String dataType, Rule newRule) {
        String key = "rule:gateway:" + gatewayId + ":sensor:" + sensorId + ":" + dataType;

        // Redis에서 SensorRule을 찾음
        SensorRule findSensorRule = redisTemplate.opsForValue().get(key);
        if (findSensorRule != null) {
            // 기존 룰 업데이트 로직
            updateOrAddRule(findSensorRule.getRules(), newRule);

            // 수정된 SensorRule을 다시 Redis에 저장
            redisTemplate.opsForValue().set(key, findSensorRule);
        } else {
            // 새로 룰이 없으면 추가하는 로직
            createAndSaveNewSensorRule(key, newRule);
        }
    }

    // 룰 업데이트 또는 추가
    private void updateOrAddRule(List<Rule> rules, Rule newRule) {
        boolean ruleUpdated = false;

        // 기존 룰을 찾아서 업데이트
        for (int i = 0; i < rules.size(); i++) {
            Rule rule = rules.get(i);

            // dataType, operator, action이 같고, value 또는 range가 다르면 추가
            if (rule.equals(newRule)) {
                // 완전히 같은 룰이면 수정할 필요 없음
                return; // 중복된 룰은 처리 안 함
            } else if (rule.getDatatype().equals(newRule.getDatatype()) &&
                    rule.getOperator().equals(newRule.getOperator()) &&
                    rule.getAction().equals(newRule.getAction())) {
                // dataType, operator, action이 같고 값이나 범위가 다르면 업데이트
                rules.set(i, newRule); // 기존 룰을 새로운 룰로 교체
                ruleUpdated = true; // 업데이트 되었으므로 flag 설정
                break; // 한 개만 업데이트하고 종료
            }
        }

        // 룰이 없으면 추가
        if (!ruleUpdated) {
            rules.add(newRule); // 룰을 추가
        }
    }

    public List<Rule> getRulesByKey(String gatewayId, String sensorId, String dataType) {
        String key = "rule:gateway:" + gatewayId + ":sensor:" + sensorId + ":" + dataType;

        // Redis에서 SensorRule을 찾음
        SensorRule sensorRule = redisTemplate.opsForValue().get(key);

        if (sensorRule == null) {
            throw new SensorRuleNotFoundException(sensorId, dataType);
        }

        // 해당하는 룰들을 반환
        return sensorRule.getRules();
    }

    // 새 SensorRule을 생성하고 저장하는 로직
    private void createAndSaveNewSensorRule(String key, Rule newRule) {
        System.out.println(">> redisTemplate = " + redisTemplate);
        System.out.println(">> redisTemplate.opsForValue() = " + redisTemplate.opsForValue());

        SensorRule sensorRule = SensorRule.ofNewRule(key, newRule);
        redisTemplate.opsForValue().set(key, sensorRule);
    }

}
