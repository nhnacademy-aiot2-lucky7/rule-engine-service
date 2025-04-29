package com.nhnacademy.ruleengineservice.common.controller;

import com.nhnacademy.ruleengineservice.common.filter.SensorRuleFilterChain;
import com.nhnacademy.ruleengineservice.sensorrule.dto.DataDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/ruleEngine")
public class SensorDataController {

    private final SensorRuleFilterChain filterChain;

    @PostMapping("/data")
    public void receiveSensorData(@RequestBody DataDTO dataDTO) {
        System.out.println(dataDTO.toString());
        Map<String, Object> sensorData = convertDataDTOToMap(dataDTO);

        boolean passed = filterChain.filter(sensorData);

        if (passed) {
            System.out.println("센서 데이터가 모든 룰을 통과했습니다!");
            // 통과했을 때 추가 동작 가능 (예: 데이터 저장, 알림 등)
        } else {
            System.out.println("센서 데이터가 룰을 통과하지 못했습니다.");
            // 불통일 때 추가 동작 가능 (예: 알림 발송, 경고 로그 등)
        }
    }

    public Map<String, Object> convertDataDTOToMap(DataDTO dataDTO) {
        Map<String, Object> sensorData = new HashMap<>();
        sensorData.put("deviceId", dataDTO.getDeviceId());
        sensorData.put("datatype", dataDTO.getDataType());
        sensorData.put("value", dataDTO.getValue());
        return sensorData;
    }
}
