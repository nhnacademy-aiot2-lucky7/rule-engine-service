package com.nhnacademy.ruleengineservice.sensordata.service.impl;

import com.nhnacademy.ruleengineservice.common.filter.SensorRuleViolationChecker;
import com.nhnacademy.ruleengineservice.enums.EventLevel;
import com.nhnacademy.ruleengineservice.message.dto.ViolatedRuleMessageDTO;
import com.nhnacademy.ruleengineservice.message.service.MessageService;
import com.nhnacademy.ruleengineservice.sensordata.dto.DataDTO;
import com.nhnacademy.ruleengineservice.sensordata.service.SensorDataProcessorService;
import com.nhnacademy.ruleengineservice.sensorrule.domain.Rule;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * {@code SensorDataProcessorServiceImpl}는 센서 데이터를 처리하고, 룰 위반이 발생하면 알림 메시지를 전송하는 서비스입니다.
 * 센서 데이터가 전달되면 해당 데이터에 대해 룰 위반 여부를 체크하고, 위반된 룰이 있으면 메시지 서비스로 알림을 보냅니다.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class SensorDataProcessorServiceImpl implements SensorDataProcessorService {

    private final SensorRuleViolationChecker violationChecker;
    private final MessageService messageService;

    /**
     * 센서 데이터를 처리하고 룰 위반 여부를 체크합니다.
     * 위반된 룰이 있으면 메시지 서비스를 통해 알림을 보냅니다.
     *
     * @param dataDTO 처리할 센서 데이터 DTO
     */
    @Override
    public void process(DataDTO dataDTO) {
        Map<String, Object> sensorData = convertDataDTOToMap(dataDTO);

        List<Rule> violatedRules = violationChecker.getViolatedRules(sensorData);

        if (!violatedRules.isEmpty()) {
            ViolatedRuleMessageDTO dto = new ViolatedRuleMessageDTO(
                    EventLevel.WARN,
                    "",
                    dataDTO.getSensorId(),
                    "센서",
                    "departmentId",
                    LocalDateTime.now());
            messageService.send(dto);
            log.debug("센서 데이터가 위반한 룰들: {}", violatedRules);
        } else {
            log.debug("센서 데이터가 모든 룰을 통과했습니다!");
        }
    }

    /**
     * {@code DataDTO}를 {@code Map} 형식으로 변환합니다.
     *
     * @param dataDTO 변환할 데이터 DTO
     * @return 변환된 센서 데이터 {@code Map}
     */
    public Map<String, Object> convertDataDTOToMap(DataDTO dataDTO) {
        Map<String, Object> sensorData = new HashMap<>();
        sensorData.put("gatewayId", dataDTO.getGatewayId());
        sensorData.put("sensorId", dataDTO.getSensorId());
        sensorData.put("dataType", dataDTO.getDataType());
        sensorData.put("value", dataDTO.getValue());
        return sensorData;
    }
}
