package com.nhnacademy.ruleengineservice.message.adapter.impl;

import com.nhnacademy.ruleengineservice.message.dto.ViolatedRuleMessageDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import com.nhnacademy.ruleengineservice.message.adapter.MessageAdapter;
import org.springframework.web.reactive.function.client.WebClientException;

@Slf4j
@Component
public class MessageAdapterImpl implements MessageAdapter {

    private final WebClient messageWebClient;

    public MessageAdapterImpl(@Qualifier("messageWebClient") WebClient messageWebClient) {
        this.messageWebClient = messageWebClient;
    }

    @Override
    public void send(ViolatedRuleMessageDTO violatedRuleMessageDTO) {
        messageWebClient.post()
                .uri("/messageService/message")
                .bodyValue(violatedRuleMessageDTO)
                .retrieve()
                .bodyToMono(Void.class)
                .doOnError(e -> log.warn("위반한 룰 메세지 전송 실패: {}", e.getMessage(), e))
                .subscribe();
    }
}
