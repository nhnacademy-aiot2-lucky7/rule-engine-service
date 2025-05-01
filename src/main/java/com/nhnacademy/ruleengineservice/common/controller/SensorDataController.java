package com.nhnacademy.ruleengineservice.common.controller;

import com.nhnacademy.ruleengineservice.sensordata.dto.DataDTO;
import com.nhnacademy.ruleengineservice.sensordata.service.SensorDataProcessorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/ruleEngine")
public class SensorDataController {

    private final SensorDataProcessorService processorService;

    @PostMapping("/data")
    public void receiveSensorData(@RequestBody DataDTO dataDTO) {
        processorService.process(dataDTO);
    }
}
