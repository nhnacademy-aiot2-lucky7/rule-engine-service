package com.nhnacademy.ruleengineservice.message.dto;

import com.nhnacademy.ruleengineservice.enums.EventLevel;
import lombok.Value;

import java.time.LocalDateTime;

@Value
public class ViolatedRuleMessageDTO {
    EventLevel eventLevel;
    String eventDetails;
    String sourceId;  // 보류
    String sourceType;  // 위 아이디의 설명?
    String departmentId;  // 추후에 센서 서비스에서 가져올 예정?
    LocalDateTime eventAt;

}
