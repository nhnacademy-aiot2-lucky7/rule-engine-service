package com.nhnacademy.ruleengineservice.sensordata.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Value;

/**
 * {@code DataDTO}는 센서 데이터 전송을 위한 데이터 전송 객체(DTO)입니다.
 * 이 클래스는 센서에서 전송되는 데이터를 포함하며, 이를 JSON 형태로 직렬화 및 역직렬화할 수 있도록 설계되었습니다.
 *
 * <p>
 * JSON 필드 이름은 {@link JsonProperty} 어노테이션을 사용하여 매핑됩니다.
 * </p>
 */
@Value
@AllArgsConstructor
public class DataDTO {

    /**
     * 게이트웨이 ID
     */
    @JsonProperty("gateway_id")
    String gatewayId;

    /**
     * 센서 ID
     */
    @JsonProperty("sensor_id")
    String sensorId;

    /**
     * 데이터 유형 (예: 온도, 습도 등)
     */
    @JsonProperty("type")
    String dataType;

    /**
     * 센서 값
     */
    @JsonProperty("value")
    Double value;

    /**
     * 센서 데이터가 수집된 타임스탬프
     */
    @JsonProperty("timestamp")
    Long timestamp;
}
