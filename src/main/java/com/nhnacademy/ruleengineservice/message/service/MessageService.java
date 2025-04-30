package com.nhnacademy.ruleengineservice.message.service;

import com.nhnacademy.ruleengineservice.message.dto.ViolatedRuleMessageDTO;

public interface MessageService {
    void send(ViolatedRuleMessageDTO violatedRuleMessageDTO);
}
