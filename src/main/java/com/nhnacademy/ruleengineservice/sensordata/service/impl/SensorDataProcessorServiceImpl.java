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

@Slf4j
@Service
@RequiredArgsConstructor
public class SensorDataProcessorServiceImpl implements SensorDataProcessorService {

    private final SensorRuleViolationChecker violationChecker;
    private final MessageService messageService;

    @Override
    public void process(DataDTO dataDTO) {
        Map<String, Object> sensorData = convertDataDTOToMap(dataDTO);

        List<Rule> violatedRules = violationChecker.getViolatedRules(sensorData);

        if (!violatedRules.isEmpty()) {
            // 위반된 룰들이 있을 경우, 해당 룰들과 센서 데이터를 메시지 서비스로 전달
            ViolatedRuleMessageDTO dto = new ViolatedRuleMessageDTO(
                    EventLevel.WARN,
                    "",
                    dataDTO.getSensorId(),
                    "센서",
                    "departmentId",
                    LocalDateTime.now());
            messageService.send(dto);  // 메시지 전송 (메시지 서비스 구현 필요)
            log.debug("센서 데이터가 위반한 룰들: {}", violatedRules);
        } else {
            log.debug("센서 데이터가 모든 룰을 통과했습니다!");
        }
    }

    public Map<String, Object> convertDataDTOToMap(DataDTO dataDTO) {
        Map<String, Object> sensorData = new HashMap<>();
        sensorData.put("gatewayId", dataDTO.getGatewayId());
        sensorData.put("sensorId", dataDTO.getSensorId());
        sensorData.put("dataType", dataDTO.getDataType());
        sensorData.put("value", dataDTO.getValue());
        return sensorData;
    }
}
