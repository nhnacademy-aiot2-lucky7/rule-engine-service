package com.nhnacademy.ruleengineservice.message.adapter;

import com.nhnacademy.ruleengineservice.message.dto.ViolatedRuleMessageDTO;

/**
 * {@code MessageService}는 센서 룰 위반 메시지를 전송하는 서비스의 인터페이스입니다.
 * 이 인터페이스는 룰 위반 발생 시 외부 시스템이나 서비스로 메시지를 전송하는 기능을 제공합니다.
 * <p>
 * 구현 클래스에서는 {@link ViolatedRuleMessageDTO} 객체를 외부 메시지 서비스로 전송하는 방식으로 기능을 수행합니다.
 * </p>
 */
public interface MessageAdapter {
    /**
     * 룰 위반 메시지를 외부 메시지 서비스로 전송합니다.
     *
     * @param violatedRuleMessageDTO 룰 위반에 대한 메시지 데이터 전송 객체
     */
    void send(ViolatedRuleMessageDTO violatedRuleMessageDTO);
}
