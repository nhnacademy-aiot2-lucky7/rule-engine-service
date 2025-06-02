package com.nhnacademy.ruleengineservice.event.producer.impl;

import com.nhnacademy.ruleengineservice.common.exception.RabbitMessageSendFailedException;
import com.nhnacademy.ruleengineservice.event.dto.ViolatedRuleEventDTO;
import com.nhnacademy.ruleengineservice.event.producer.EventProducer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class EventProducerImpl implements EventProducer {

    private final RabbitTemplate rabbitTemplate;

    @Value("${mq.exchange.event}")
    private String exchange;

    @Value("${mq.routing-key.event}")
    private String routingKey;

    @Override
    public void sendEvent(ViolatedRuleEventDTO dto) {
        try {
            rabbitTemplate.convertAndSend(exchange, routingKey, dto);
            log.info("event 전송: {}", dto.getEventAt());
        } catch (AmqpException e) {
            throw new RabbitMessageSendFailedException();
        }
    }
}
