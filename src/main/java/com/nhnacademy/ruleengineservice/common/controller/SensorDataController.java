package com.nhnacademy.ruleengineservice.common.controller;

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


    @PostMapping("/data")
    public void receiveSensorData(@RequestBody DataDTO dataDTO) {
        System.out.println(dataDTO.toString());
        Map<String, Object> sensorData = convertDataDTOToMap(dataDTO);
    }

    public Map<String, Object> convertDataDTOToMap(DataDTO dataDTO) {
        Map<String, Object> sensorData = new HashMap<>();
        sensorData.put("deviceId", dataDTO.getDeviceId());
        sensorData.put("datatype", dataDTO.getDataType());
        sensorData.put("value", dataDTO.getValue());
        return sensorData;
    }

}
