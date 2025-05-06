package com.nhnacademy.ruleengineservice.enums;

/**
 * {@code ActionType}은 룰 엔진에서 발생할 수 있는 액션 타입을 정의하는 열거형입니다.
 * <p>
 * 이 열거형은 시스템이 센서 룰을 위반했을 때 실행할 수 있는 여러 가지 액션을 정의합니다.
 * 각 액션은 문자열 값과 설명을 가지고 있으며, 이를 통해 다양한 처리 방식을 구분합니다.
 * </p>
 *
 * <p>액션 타입:</p>
 * <ul>
 *     <li>{@code SEND_ALERT} - 알림을 전송하는 액션</li>
 *     <li>{@code LOG_WARNING} - 경고 로그를 기록하는 액션</li>
 *     <li>{@code SHUTDOWN_DEVICE} - 장치를 강제 종료하는 액션</li>
 * </ul>
 *
 */
public enum ActionType {
    /**
     * 알림을 전송하는 액션.
     */
    SEND_ALERT("send_alert", "알림전송"),

    /**
     * 경고 로그를 기록하는 액션.
     */
    LOG_WARNING("log_warning","warning 로그발생"),

    /**
     * 장치를 강제 종료하는 액션.
     */
    SHUTDOWN_DEVICE("shutdown_device", "장치 강제 종료");

    private final String value;
    private final String desc;

    /**
     * 생성자
     *
     * @param value 액션의 값
     * @param desc 액션에 대한 설명
     */
    ActionType(String value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    /**
     * 액션의 값을 반환합니다.
     *
     * @return 액션의 값
     */
    public String getValue() {
        return value;
    }

    /**
     * 액션에 대한 설명을 반환합니다.
     *
     * @return 액션에 대한 설명
     */
    public String getDesc() {
        return desc;
    }
}