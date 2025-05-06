package com.nhnacademy.ruleengineservice.sensorrule.service;

import com.nhnacademy.ruleengineservice.enums.Operator;
import com.nhnacademy.ruleengineservice.enums.RuleType;
import com.nhnacademy.ruleengineservice.sensordata.dto.DataDTO;
import com.nhnacademy.ruleengineservice.sensorrule.domain.Rule;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * SensorRuleViolationChecker 클래스는 센서 데이터에 대해 등록된 룰을 기반으로
 * 위반된 룰을 판별하는 서비스 클래스입니다.
 *
 * <p>센서 데이터에는 gatewayId, sensorId, dataType, value 값이 포함되어야 하며,
 * 해당 정보로 룰을 조회한 후 센서 값이 조건을 충족하지 못하는 경우 위반으로 간주합니다.</p>
 *
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class SensorRuleViolationService {

    private final SensorRuleService sensorRuleService;

    /**
     * 주어진 센서 데이터를 기반으로 위반된 룰 목록을 반환합니다.
     *
     * @param dataDTO 센서 데이터 (gatewayId, sensorId, dataType, value 포함)
     * @return 위반된 {@link Rule} 객체 리스트
     */
    public List<Rule> getViolatedRules(DataDTO dataDTO) {
        String gatewayId = dataDTO.getGatewayId();
        String sensorId = dataDTO.getSensorId();
        String dataType = dataDTO.getDataType();
        Double sensorValue = dataDTO.getValue();

        List<Rule> rules = sensorRuleService.getRulesByKey(gatewayId, sensorId, dataType);

        return rules.stream()
                .filter(rule -> {
                    RuleType ruleType = rule.getRuletype();
                    Operator operator = rule.getOperator();
                    double targetValue = rule.getValue();
                    boolean passed = operator.compare(sensorValue, targetValue);
                    log.info("▶ Rule 위반검사 - 타입: {}, 연산자: {}, 기준값: {}, 센서값: {} → 결과: {}",
                            ruleType,
                            operator.getSymbol(),
                            String.format("%.2f", targetValue),
                            String.format("%.2f", sensorValue),
                            !passed ? "통과" : "위반");
                    return passed;
                })
                .toList();
    }
}
