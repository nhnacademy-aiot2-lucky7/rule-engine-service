package com.nhnacademy.ruleengineservice.sensordata.service;

import com.nhnacademy.ruleengineservice.sensordata.dto.DataDTO;

public interface SensorDataProcessorService {
    void process(DataDTO dataDTO);
}
