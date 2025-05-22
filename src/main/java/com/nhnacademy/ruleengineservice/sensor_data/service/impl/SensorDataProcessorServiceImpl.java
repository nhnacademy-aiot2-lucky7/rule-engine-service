package com.nhnacademy.ruleengineservice.sensor_data.service.impl;

import com.nhnacademy.ruleengineservice.enums.RuleType;
import com.nhnacademy.ruleengineservice.event.producer.EventProducer;
import com.nhnacademy.ruleengineservice.gateway.adapter.GatewayAdapter;
import com.nhnacademy.ruleengineservice.sensor_rule.domain.SensorRule;
import com.nhnacademy.ruleengineservice.sensor_rule.service.SensorRuleViolationService;
import com.nhnacademy.ruleengineservice.enums.EventLevel;
import com.nhnacademy.ruleengineservice.event.dto.ViolatedRuleEventDTO;
import com.nhnacademy.ruleengineservice.sensor_data.dto.DataDTO;
import com.nhnacademy.ruleengineservice.sensor_data.service.SensorDataProcessorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * {@code SensorDataProcessorServiceImpl}는 센서 데이터를 처리하고, 룰 위반이 발생하면 알림 메시지를 전송하는 서비스입니다.
 * 센서 데이터가 전달되면 해당 데이터에 대해 룰 위반 여부를 체크하고, 위반된 룰이 있으면 메시지 서비스로 알림을 보냅니다.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class SensorDataProcessorServiceImpl implements SensorDataProcessorService {

    private final SensorRuleViolationService violationService;
    private final EventProducer eventProducer;
    private final GatewayAdapter gatewayAdapter;

    /**
     * 센서 데이터를 처리하고 룰 위반 여부를 체크합니다.
     * 위반된 룰이 있으면 메시지 서비스를 통해 알림을 보냅니다.
     *
     * @param dataDTO 처리할 센서 데이터 DTO
     */
    @Override
    public void process(DataDTO dataDTO) {
        try {
            List<SensorRule> violatedRules = violationService.getViolatedRules(dataDTO);

            if (!violatedRules.isEmpty()) {
                for (SensorRule violatedRule : violatedRules) {
                    String eventDetail = (violatedRule.getRuleType().equals(RuleType.AVG))
                            ? createAVGEventDetail(dataDTO, violatedRule)
                            : createEventDetail(dataDTO, violatedRule);

                    ViolatedRuleEventDTO dto = toEventDTO(dataDTO, eventDetail);
                    eventProducer.sendEvent(dto);
                }

                log.info("센서 데이터가 위반한 룰들: {}", violatedRules);
            } else {
                log.info("센서 데이터가 모든 룰을 통과했습니다!");
            }

        } catch (Exception e) {
            log.warn("룰 처리 중 예외 발생: {}", e.getMessage(), e);
            // 예외 삼키고 아무것도 하지 않음
        }
    }

    private String createEventDetail(DataDTO dataDTO, SensorRule violatedRule) {
        return String.format("센서 [%s]의 [%s]데이터에 대한 %s 룰 위반: 데이터값 %.2f은(는) %.2f%s %s.",
                dataDTO.getSensorId(),
                violatedRule.getDataTypeKrName(),
                violatedRule.getRuleType(),
                dataDTO.getValue(),
                violatedRule.getValue(),
                violatedRule.getOperator().getDescription(),
                violatedRule.getAction().getDesc()
        );
    }

    private String createAVGEventDetail(DataDTO dataDTO, SensorRule violatedRule) {
        return String.format("센서 [%s]의 [%s]데이터에 대한 %s 룰 위반: 데이터값 %.2f은(는) %.2f~%.2f %s %s.",
                dataDTO.getSensorId(),
                violatedRule.getDataTypeKrName(),
                violatedRule.getRuleType(),
                dataDTO.getValue(),
                violatedRule.getMinValue(),
                violatedRule.getMaxValue(),
                violatedRule.getOperator().getDescription(),
                violatedRule.getAction().getDesc()
        );
    }

    private ViolatedRuleEventDTO toEventDTO(DataDTO dataDTO, String eventDetail) {
        return new ViolatedRuleEventDTO(
                EventLevel.WARN,
                eventDetail,
                dataDTO.getSensorId(),
                "센서",
                gatewayAdapter.getDepartmentIdByGatewayId(dataDTO.getGatewayId()),
                LocalDateTime.now()
        );
    }
}
