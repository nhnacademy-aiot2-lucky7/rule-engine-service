package com.nhnacademy.ruleengineservice.enums;

/**
 * {@code EventLevel}은 이벤트의 중요도를 나타내는 열거형입니다.
 * <p>
 * 이 열거형은 이벤트의 수준을 나타내며, 각 수준은 다음과 같습니다:
 * </p>
 * <ul>
 *     <li>{@code INFO} - 정보성 이벤트</li>
 *     <li>{@code WARN} - 경고 이벤트</li>
 *     <li>{@code ERROR} - 오류 이벤트</li>
 * </ul>
 *
 */
public enum EventLevel {
    INFO,
    WARN,
    ERROR
}
