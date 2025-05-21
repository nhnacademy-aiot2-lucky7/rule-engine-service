package com.nhnacademy.ruleengineservice.event.producer.impl;

import com.nhnacademy.ruleengineservice.enums.EventLevel;
import com.nhnacademy.ruleengineservice.event.dto.ViolatedRuleEventDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class EventProducerImplTest {

    @Mock
    private RabbitTemplate rabbitTemplate;

    @InjectMocks
    private EventProducerImpl eventProducer;

    @Test
    void testSendEvent() {
        ViolatedRuleEventDTO dto = new ViolatedRuleEventDTO(
                EventLevel.WARN,
                "센서 sensor1의 MAX 룰 위반: 데이터값 70.00은(는) 60.00을 초과하였으므로 알림전송.",
                "sensor1",
                "센서",
                "부서1",
                LocalDateTime.now()
        );

        eventProducer.sendEvent(dto);

        verify(rabbitTemplate, times(1)).convertAndSend( isNull(), isNull(), eq(dto));
    }
}