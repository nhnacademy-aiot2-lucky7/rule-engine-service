package com.nhnacademy.ruleengineservice.sensorrule.service.impl;

import com.nhnacademy.ruleengineservice.common.exception.SensorRuleNotFoundException;
import com.nhnacademy.ruleengineservice.sensorrule.domain.Rule;
import com.nhnacademy.ruleengineservice.sensorrule.domain.SensorRule;
import com.nhnacademy.ruleengineservice.sensorrule.service.SensorRuleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 센서 룰을 Redis에 저장하고, 해당 룰을 관리하는 서비스 구현 클래스입니다.
 * 센서에 대한 룰을 저장, 업데이트, 삭제하는 기능을 제공합니다.
 *
 * <p>
 * 이 클래스는 Redis를 사용하여 센서 룰을 저장하고, 데이터 유형, 연산자, 액션을 기반으로 룰을 업데이트하거나 새로 추가합니다.
 * 또한, 저장된 룰을 가져오는 기능도 제공합니다.
 * </p>
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class SensorRuleServiceImpl implements SensorRuleService {

    private final RedisTemplate<String, SensorRule> redisTemplate;

    /**
     * 지정된 게이트웨이, 센서, 데이터 타입에 대한 룰을 Redis에 저장하거나 업데이트합니다.
     *
     * <p>
     * 만약 이미 해당 키에 룰이 존재하면 기존 룰을 업데이트하고, 없으면 새 룰을 추가합니다.
     * </p>
     *
     * @param gatewayId  센서가 연결된 게이트웨이의 ID
     * @param sensorId   센서의 ID
     * @param dataType   데이터 타입 (예: 온도, 습도 등)
     * @param newRule    새로 추가할 룰 객체
     */
    public void saveSensorRules(String gatewayId, String sensorId, String dataType, Rule newRule) {
        String key = "rule:gateway:" + gatewayId + ":sensor:" + sensorId + ":" + dataType;

        SensorRule findSensorRule = redisTemplate.opsForValue().get(key);
        if (findSensorRule != null) {
            updateOrAddRule(findSensorRule.getRules(), newRule);

            redisTemplate.opsForValue().set(key, findSensorRule);
        } else {
            createAndSaveNewSensorRule(key, newRule);
        }
    }

    /**
     * 기존 룰 리스트에서 새로운 룰을 업데이트하거나 추가합니다.
     *
     * <p>
     * 룰이 동일하면 아무런 변화 없이 리턴되고, 값이나 범위가 다른 경우에만 업데이트됩니다.
     * </p>
     *
     * @param rules    기존 룰 리스트
     * @param newRule  새로 추가할 룰
     */
    private void updateOrAddRule(List<Rule> rules, Rule newRule) {
        boolean ruleUpdated = false;

        for (int i = 0; i < rules.size(); i++) {
            Rule rule = rules.get(i);

            if (rule.equals(newRule)) {
                return;
            } else if (rule.getDatatype().equals(newRule.getDatatype()) &&
                    rule.getOperator().equals(newRule.getOperator()) &&
                    rule.getAction().equals(newRule.getAction())) {
                rules.set(i, newRule);
                ruleUpdated = true;
                break;
            }
        }

        if (!ruleUpdated) {
            rules.add(newRule);
        }
    }

    /**
     * 주어진 키에 해당하는 룰을 Redis에서 조회하여 반환합니다.
     *
     * @param gatewayId  센서가 연결된 게이트웨이의 ID
     * @param sensorId   센서의 ID
     * @param dataType   데이터 타입
     * @return 해당하는 센서 룰 리스트
     * @throws SensorRuleNotFoundException 룰을 찾을 수 없을 경우 예외 발생
     */
    public List<Rule> getRulesByKey(String gatewayId, String sensorId, String dataType) {
        String key = "rule:gateway:" + gatewayId + ":sensor:" + sensorId + ":" + dataType;

        SensorRule sensorRule = redisTemplate.opsForValue().get(key);

        if (sensorRule == null) {
            throw new SensorRuleNotFoundException(sensorId, dataType);
        }

        return sensorRule.getRules();
    }

    /**
     * 새 센서 룰을 생성하고 Redis에 저장합니다.
     *
     * @param key     Redis에 저장할 키
     * @param newRule 새로 추가할 룰 객체
     */
    private void createAndSaveNewSensorRule(String key, Rule newRule) {
        log.debug(">> redisTemplate = {}", redisTemplate);
        log.debug(">> redisTemplate.opsForValue() = {}", redisTemplate.opsForValue());

        SensorRule sensorRule = SensorRule.ofNewRule(key, newRule);
        redisTemplate.opsForValue().set(key, sensorRule);
    }

}
