package com.nhnacademy.ruleengineservice.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * WebClientConfig 클래스는 WebClient 빈을 등록하는 설정 클래스입니다.
 * <p>
 * WebClient는 Spring WebFlux에서 제공하는 비동기 HTTP 클라이언트이며,
 * 기본 baseUrl을 {@code http://localhost:8080}으로 설정하여 서버 간 통신에 사용됩니다.
 * </p>
 */
@Configuration
public class WebClientConfig {

    /**
     * WebClient 빈을 생성합니다.
     * <p>
     * 해당 WebClient는 {@code http://localhost:8080}을 기본 baseUrl로 설정합니다.
     * </p>
     *
     * @return WebClient 객체
     */
    @Bean
    public WebClient webClient() {
        return WebClient.builder()
                .baseUrl("http://localhost:8080")
                .build();
    }
}
