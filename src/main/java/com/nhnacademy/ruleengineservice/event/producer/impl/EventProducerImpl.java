package com.nhnacademy.ruleengineservice.event.producer.impl;

import com.nhnacademy.ruleengineservice.common.exception.RabbitMessageSendFailedException;
import com.nhnacademy.ruleengineservice.event.dto.ViolatedRuleEventDTO;
import com.nhnacademy.ruleengineservice.event.producer.EventProducer;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

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
        } catch (AmqpException e) {
            throw new RabbitMessageSendFailedException();
        }
    }
}
