package com.nhnacademy.ruleengineservice.sensor_data.service;

import com.nhnacademy.ruleengineservice.sensor_data.dto.DataDTO;

/**
 * {@code SensorDataProcessorService}는 센서 데이터를 처리하는 서비스 인터페이스입니다.
 * 센서 데이터를 처리하여 룰 위반을 체크하고, 위반된 룰이 있을 경우 알림을 전송하는 역할을 합니다.
 */
public interface SensorDataProcessorService {

    /**
     * 센서 데이터를 처리합니다.
     * 데이터가 룰을 위반했는지 체크하고, 위반이 있을 경우 알림을 전송합니다.
     *
     * @param dataDTO 처리할 센서 데이터 DTO
     */
    void process(DataDTO dataDTO);
}
