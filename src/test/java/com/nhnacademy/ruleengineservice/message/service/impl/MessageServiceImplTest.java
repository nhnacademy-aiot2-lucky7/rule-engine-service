package com.nhnacademy.ruleengineservice.message.service.impl;

import com.nhnacademy.ruleengineservice.enums.EventLevel;
import com.nhnacademy.ruleengineservice.message.dto.ViolatedRuleMessageDTO;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mock;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class MessageServiceImplTest {

    @Mock
    private WebClient webClient;

    @BeforeEach
    void setUp() {
        ViolatedRuleMessageDTO newMessage = new ViolatedRuleMessageDTO(
                EventLevel.ERROR,
                "해당 센서에서 들어온 값 60.0도는 임계값을 ",
                "sensor1",
                "센서1",
                "부서",
                LocalDateTime.now()
        );
    }

}