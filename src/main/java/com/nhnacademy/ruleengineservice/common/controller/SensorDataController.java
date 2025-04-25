package com.nhnacademy.ruleengineservice.common.controller;

import com.nhnacademy.ruleengineservice.datatype.cache.DataCache;
import com.nhnacademy.ruleengineservice.sensorrule.repository.SensorRuleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


@RestController
@RequiredArgsConstructor
@RequestMapping("/ruleEngine")
public class SensorDataController {

    private final DataCache dataCache;
    private final SensorRuleRepository sensorRuleRepository;

    @PostMapping("/data")
    public void receiveSensorData(@RequestBody Map<String, Object> payload) {
        String typeName = (String) payload.get("datatype");
        String typeDesc = (String) payload.get("desc");

        dataCache.saveIfAbsent(typeName, typeDesc);
    }

    @GetMapping("/threshold")
    public ResponseEntity<Map<String, Object>> getThreshold(@RequestBody Map<String, Object> payload) {
        String SensorId = (String) payload.get("sensorId");
        String datatype = (String) payload.get("dataType");


    }
}
