package com.nhnacademy.ruleengineservice.message.adapter.impl;

import com.nhnacademy.ruleengineservice.enums.EventLevel;
import com.nhnacademy.ruleengineservice.message.dto.ViolatedRuleMessageDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MessageAdapterImplTest {

    @Mock
    WebClient webClient;

    @Mock
    WebClient.RequestBodyUriSpec uriSpec;

    @Mock
    WebClient.RequestBodySpec bodySpec;

    @Mock
    WebClient.RequestHeadersSpec headersSpec;

    @Mock
    WebClient.ResponseSpec responseSpec;

    @InjectMocks
    MessageAdapterImpl messageAdapter;

    @Test
    @DisplayName("메세지 서비스로 에러 메세지 전송")
    void sendMessage() {
        ViolatedRuleMessageDTO messageDTO = new ViolatedRuleMessageDTO(
                EventLevel.WARN,
                "센서 sensor1의 MAX 룰 위반: 데이터값 60.00은(는) 50.00을 초과하므로 알림전송.",
                "sensor1",
                "센서",
                "인사관리팀",
                LocalDateTime.now()
        );

        when(webClient.post()).thenReturn(uriSpec);
        when(uriSpec.uri("/messageService/message")).thenReturn(bodySpec);
        when(bodySpec.bodyValue(messageDTO)).thenReturn(headersSpec);
        when(headersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(Void.class)).thenReturn(Mono.empty());

        messageAdapter.send(messageDTO);

        verify(webClient).post();
        verify(uriSpec).uri("/messageService/message");
        verify(bodySpec).bodyValue(messageDTO);
        verify(headersSpec).retrieve();
        verify(responseSpec).bodyToMono(Void.class);
    }
}