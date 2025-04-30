package com.nhnacademy.ruleengineservice.message.service.impl;

import com.nhnacademy.ruleengineservice.message.dto.ViolatedRuleMessageDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import com.nhnacademy.ruleengineservice.message.service.MessageService;

@Service
@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService {
    private final WebClient webClient;
    private final MessageService messageService;

    @Override
    public void send(ViolatedRuleMessageDTO violatedRuleMessageDTO) {
        webClient.post()
                .uri("/messageService/message")
                .bodyValue(violatedRuleMessageDTO)
                .retrieve()
                .bodyToMono(Void.class)
                .subscribe();
    }
}
