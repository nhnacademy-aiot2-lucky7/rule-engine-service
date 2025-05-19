package com.nhnacademy.ruleengineservice.event.dto;

import com.nhnacademy.ruleengineservice.enums.EventLevel;
import lombok.Value;

import java.time.LocalDateTime;

/**
 * {@code ViolatedRuleMessageDTO}는 센서 룰 위반 이벤트에 대한 메시지 정보를 담는 DTO(Data Transfer Object)입니다.
 * <p>
 * 이 객체는 룰 위반이 발생한 이벤트에 대한 세부 정보와 함께 이벤트의 수준, 발생 시간 등을 포함합니다.
 * </p>
 *
 * <ul>
 *     <li>{@code eventLevel} - 이벤트 수준 (INFO, WARN, ERROR)</li>
 *     <li>{@code eventDetails} - 이벤트의 상세 정보</li>
 *     <li>{@code sourceId} - 이벤트 발생 원본의 ID</li>
 *     <li>{@code sourceType} - 이벤트 발생 원본의 유형</li>
 *     <li>{@code departmentId} - 해당 이벤트와 관련된 부서 ID</li>
 *     <li>{@code eventAt} - 이벤트 발생 시간</li>
 * </ul>
 */
@Value
public class ViolatedRuleEventDTO {
    /**
     * 이벤트 수준 (INFO, WARN, ERROR)
     */
    EventLevel eventLevel;

    /**
     * 이벤트의 상세 정보
     */
    String eventDetails;

    /**
     * 이벤트 발생 원본의 ID
     */
    String sourceId;

    /**
     * 이벤트 발생 원본의 유형
     */
    String sourceType;

    /**
     * 해당 이벤트와 관련된 부서 ID
     */
    String departmentId;

    /**
     * 이벤트 발생 시간
     */
    LocalDateTime eventAt;
}
