package com.nhnacademy.ruleengineservice.event.producer;

import com.nhnacademy.ruleengineservice.event.dto.ViolatedRuleEventDTO;

public interface EventProducer {
    void sendEvent(ViolatedRuleEventDTO dto);
}
