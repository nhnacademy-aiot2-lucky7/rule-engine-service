package com.nhnacademy.ruleengineservice.message.service.impl;

import com.nhnacademy.ruleengineservice.enums.EventLevel;
import com.nhnacademy.ruleengineservice.message.dto.ViolatedRuleMessageDTO;
import com.nhnacademy.ruleengineservice.message.service.MessageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class MessageServiceImplTest {

    @Mock
    private WebClient webClient;

    @InjectMocks
    private MessageService messageService;

    @BeforeEach
    void setUp() {

    }

    @Test
    void processTest() {
        ViolatedRuleMessageDTO newMessage = new ViolatedRuleMessageDTO(
                EventLevel.ERROR,
                "sensor1에서 들어온 값 60.0도는 최대 임계값인 50.0도를 초과하였습니다!",
                "sensor1",
                "센서1",
                "부서",
                LocalDateTime.now()
        );

//        Mockito.doAnswer(invocation -> {
//                    return null;
//                })
//                .when()

        messageService.send(newMessage);


    }
}