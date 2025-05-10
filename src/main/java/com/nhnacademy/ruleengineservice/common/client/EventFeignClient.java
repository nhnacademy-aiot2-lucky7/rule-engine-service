package com.nhnacademy.ruleengineservice.common.client;

import com.nhnacademy.ruleengineservice.event.dto.ViolatedRuleEventDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "event-service", url = "${event.service.url}")
public interface EventFeignClient {

    @PostMapping("/eventService/events")
    void sendViolatedRuleEvent(@RequestBody ViolatedRuleEventDTO violatedRuleEventDTO);
}