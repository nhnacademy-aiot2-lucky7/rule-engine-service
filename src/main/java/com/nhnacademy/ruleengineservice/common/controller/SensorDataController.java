package com.nhnacademy.ruleengineservice.common.controller;

import com.nhnacademy.ruleengineservice.sensorrule.repository.SensorRuleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;


@RestController
@RequiredArgsConstructor
@RequestMapping("/ruleEngine")
public class SensorDataController {

    private final SensorRuleRepository sensorRuleRepository;

    @PostMapping("/data")
    public void receiveSensorData(@RequestBody Map<String, Object> payload) {
        Map<String, Object> data = (Map<String, Object>) payload.get("data");

        String sensorId = (String) data.get("sensorId");
        String typeName = (String) data.get("datatype");
        String typeDesc = (String) data.get("desc");
        String place = (String) data.get("place");
        Integer value = (Integer) data.get("value");

        // 예시로 찍어볼게요
        System.out.println("sensorId = " + sensorId);
        System.out.println("typeName = " + typeName);
        System.out.println("typeDesc = " + typeDesc);
        System.out.println("place = " + place);
        System.out.println("value = " + value);
    }
}
