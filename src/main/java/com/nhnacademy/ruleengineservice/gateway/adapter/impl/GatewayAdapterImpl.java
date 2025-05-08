package com.nhnacademy.ruleengineservice.gateway.adapter.impl;

import com.nhnacademy.ruleengineservice.gateway.adapter.GatewayAdapter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientException;

@Slf4j
@Component
public class GatewayAdapterImpl implements GatewayAdapter {

    private final WebClient gatewayWebClient;

    public GatewayAdapterImpl(@Qualifier("gatewayWebClient") WebClient gatewayWebClient) {
        this.gatewayWebClient = gatewayWebClient;
    }

    @Override
    public String getDepartmentIdByGatewayId(String gatewayId) {
        try {
            return gatewayWebClient.get()
                    .uri("/gateway-service/gateway/{gatewayId}/department", gatewayId)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block(); // 동기적으로 기다림
        } catch (WebClientException e) {
            log.warn("게이트웨이에서 departmentId 조회 실패: {}", e.getMessage(), e);
            return null;
        }
    }
}
