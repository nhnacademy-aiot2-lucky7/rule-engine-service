package com.nhnacademy.ruleengineservice.event.adapter.impl;

import com.nhnacademy.ruleengineservice.common.client.EventFeignClient;
import com.nhnacademy.ruleengineservice.enums.EventLevel;
import com.nhnacademy.ruleengineservice.event.dto.ViolatedRuleEventDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class EventAdapterImplTest {

    @Mock
    EventFeignClient eventFeignClient;

    @InjectMocks
    EventAdapterImpl eventAdapter;

    @Test
    @DisplayName("메세지 서비스로 에러 메세지 전송")
    void sendEvent() {
        ViolatedRuleEventDTO messageDTO = new ViolatedRuleEventDTO(
                EventLevel.WARN,
                "센서 sensor1의 MAX 룰 위반: 데이터값 60.00은(는) 50.00을 초과하므로 알림전송.",
                "sensor1",
                "센서",
                "인사관리팀",
                LocalDateTime.now()
        );

        eventAdapter.send(messageDTO);

        verify(eventFeignClient).sendViolatedRuleEvent(messageDTO);
    }
}