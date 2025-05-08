package com.nhnacademy.ruleengineservice.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Bean(name = "gatewayWebClient")
    public WebClient gatewayWebClient(WebClient.Builder builder) {
        return builder
                .baseUrl("http://gateway-service:10241")
                .build();
    }

    @Bean(name = "messageWebClient")
    public WebClient messageWebClient(WebClient.Builder builder) {
        return builder
                .baseUrl("http://message-service:8080")
                .build();
    }
}
