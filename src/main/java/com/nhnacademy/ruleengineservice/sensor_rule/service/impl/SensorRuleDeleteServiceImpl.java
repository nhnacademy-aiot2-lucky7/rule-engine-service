package com.nhnacademy.ruleengineservice.sensor_rule.service.impl;

import com.nhnacademy.ruleengineservice.common.exception.SensorRuleException;
import com.nhnacademy.ruleengineservice.dashboard.dto.RuleDeleteRequest;
import com.nhnacademy.ruleengineservice.enums.EventLevel;
import com.nhnacademy.ruleengineservice.event.dto.ViolatedRuleEventDTO;
import com.nhnacademy.ruleengineservice.event.producer.EventProducer;
import com.nhnacademy.ruleengineservice.sensor_rule.service.SensorRuleDeleteService;
import com.nhnacademy.ruleengineservice.sensor_rule.service.SensorRuleService;
import io.micrometer.common.util.StringUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Objects;


@Slf4j
@Service
@RequiredArgsConstructor
public class SensorRuleDeleteServiceImpl implements SensorRuleDeleteService {

    private static final String EVENT_DETAIL_TEMPLATE = "센서 [%s]의 [%s] 데이터에 대한 룰이 삭제되었습니다.";
    private static final String SENSOR_RULE_DELETION_MISSING_REQUIRED = "SensorRule 삭제 실패: 필수값이 누락되었습니다.";
    private static final String SOURCE_TYPE = "센서";

    private final SensorRuleService sensorRuleService;
    private final EventProducer eventProducer;

    @Override
    public void deleteRules(RuleDeleteRequest request) {
        validateRequiredFields(request);

        sensorRuleService.deleteSensorAllRules(request.getGatewayId(), request.getSensorId(), request.getDataTypeEnName());

        sendRuleDeletedEvent(request);
    }

    private void validateRequiredFields(RuleDeleteRequest request) {
        if (Objects.isNull(request.getGatewayId()) ||
                StringUtils.isBlank(request.getGatewayId().toString()) ||
                StringUtils.isBlank(request.getSensorId()) ||
                StringUtils.isBlank(request.getDepartmentId()) ||
                StringUtils.isBlank(request.getDataTypeEnName()) ||
                StringUtils.isBlank(request.getDataTypeKrName())) {
            throw new SensorRuleException(SENSOR_RULE_DELETION_MISSING_REQUIRED);
        }
    }

    private void sendRuleDeletedEvent(RuleDeleteRequest deleteRequest) {
        String eventDetail = String.format(EVENT_DETAIL_TEMPLATE,
                deleteRequest.getSensorId(),
                deleteRequest.getDataTypeKrName()
        );

        ViolatedRuleEventDTO eventDTO = new ViolatedRuleEventDTO(
                EventLevel.INFO,
                eventDetail,
                deleteRequest.getSensorId(),
                SOURCE_TYPE,
                deleteRequest.getDepartmentId(),
                LocalDateTime.now()
        );

        eventProducer.sendEvent(eventDTO);
        log.info("Event message: {}", eventDetail);
    }
}
