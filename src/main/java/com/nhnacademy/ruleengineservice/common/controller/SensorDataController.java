package com.nhnacademy.ruleengineservice.common.controller;

import com.nhnacademy.ruleengineservice.sensor_data.dto.DataDTO;
import com.nhnacademy.ruleengineservice.sensor_data.service.SensorDataProcessorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * SensorDataController 클래스는 센서 데이터 수신을 처리하는 REST 컨트롤러입니다.
 * <p>
 * 클라이언트로부터 센서 데이터를 POST 방식으로 수신하며, 수신된 데이터를
 * {@link SensorDataProcessorService}를 통해 처리합니다.
 * </p>
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/rule_engine")
public class SensorDataController {

    private final SensorDataProcessorService processorService;

    /**
     * 센서 데이터를 수신하고 처리하는 엔드포인트입니다.
     * <p>
     * 클라이언트로부터 JSON 형식의 센서 데이터를 {@link DataDTO} 객체로 수신하며,
     * 내부 서비스인 {@link SensorDataProcessorService}를 통해 처리합니다.
     * </p>
     *
     * @param dataDTO 수신된 센서 데이터 DTO
     */
    @PostMapping("/data")
    public ResponseEntity<Void> receiveSensorData(@RequestBody DataDTO dataDTO) {
        log.info("수신 데이터: [{}]", dataDTO);
        processorService.process(dataDTO);
        return ResponseEntity.ok().build();
    }
}
