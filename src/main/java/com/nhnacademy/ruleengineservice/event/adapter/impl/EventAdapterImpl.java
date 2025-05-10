package com.nhnacademy.ruleengineservice.event.adapter.impl;

import com.nhnacademy.ruleengineservice.common.client.EventFeignClient;
import com.nhnacademy.ruleengineservice.event.dto.ViolatedRuleEventDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import com.nhnacademy.ruleengineservice.event.adapter.EventAdapter;

@Slf4j
@Component
public class EventAdapterImpl implements EventAdapter {

    private final EventFeignClient eventFeignClient;

    public EventAdapterImpl(EventFeignClient eventFeignClient) {
        this.eventFeignClient = eventFeignClient;
    }

    @Override
    public void send(ViolatedRuleEventDTO violatedRuleEventDTO) {
        try {
            eventFeignClient.sendViolatedRuleEvent(violatedRuleEventDTO);
        } catch (Exception e) {
            log.warn("[Feign] 메시지 전송 실패: {}", e.getMessage(), e);
        }
    }
}
