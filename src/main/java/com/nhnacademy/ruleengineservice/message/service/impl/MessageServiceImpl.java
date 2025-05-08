package com.nhnacademy.ruleengineservice.message.service.impl;

import com.nhnacademy.ruleengineservice.message.dto.ViolatedRuleMessageDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import com.nhnacademy.ruleengineservice.message.service.MessageService;

/**
 * {@code MessageServiceImpl}은 {@link MessageService} 인터페이스의 구현체로,
 * 센서 룰 위반 메시지를 외부 메시지 서비스로 전송하는 기능을 제공합니다.
 *
 * <p>
 * 이 서비스는 {@link WebClient}를 사용하여 비동기적으로 HTTP POST 요청을 보내며,
 * 룰 위반 메시지를 전송합니다. {@code send} 메서드는 메시지 DTO를 받아 외부 서비스에 전송합니다.
 * </p>
 */
@Service
@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService {

    /**
     * WebClient는 비동기 HTTP 요청을 처리하는 클라이언트입니다.
     */
    private final WebClient webClient;

    /**
     * 룰 위반 메시지를 외부 메시지 서비스로 전송합니다.
     *
     * @param violatedRuleMessageDTO 룰 위반에 대한 메시지 데이터 전송 객체
     */
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
