package com.nhnacademy.ruleengineservice.common.controller;

import com.nhnacademy.ruleengineservice.common.filter.SensorRuleFilterChain;
import com.nhnacademy.ruleengineservice.common.filter.SensorRuleViolationChecker;
import com.nhnacademy.ruleengineservice.enums.EventLevel;
import com.nhnacademy.ruleengineservice.message.dto.ViolatedRuleMessageDTO;
import com.nhnacademy.ruleengineservice.message.service.MessageService;
import com.nhnacademy.ruleengineservice.message.service.impl.MessageServiceImpl;
import com.nhnacademy.ruleengineservice.sensorrule.domain.Rule;
import com.nhnacademy.ruleengineservice.sensorrule.dto.DataDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/ruleEngine")
public class SensorDataController {

    private final SensorRuleViolationChecker violationChecker;
    private final MessageServiceImpl messageService;

    @PostMapping("/data")
    public void receiveSensorData(@RequestBody DataDTO dataDTO) {
        System.out.println(dataDTO.toString());
        Map<String, Object> sensorData = convertDataDTOToMap(dataDTO);

        // 룰 위반 체크
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
            System.out.println("센서 데이터가 위반한 룰들: " + violatedRules);
        } else {
            System.out.println("센서 데이터가 모든 룰을 통과했습니다!");
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
