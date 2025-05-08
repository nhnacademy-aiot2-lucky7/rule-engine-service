package com.nhnacademy.ruleengineservice.gateway.adapter.impl;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class GatewayAdapterImplTest {

    @Mock
    WebClient webClient;

    @Mock
    WebClient.RequestHeadersUriSpec uriSpec;

    @Mock
    WebClient.RequestHeadersSpec headersSpec;

    @Mock
    WebClient.ResponseSpec responseSpec;

    @InjectMocks
    GatewayAdapterImpl gatewayAdapter;

    @Test
    @DisplayName("gatewayId로 departmentId 가져오기 - 성공")
    void getDepartmentIdByGatewayId_successed() {
        String gatewayId = "gateway1";
        String expectedDeptId = "department1";

        when(webClient.get()).thenReturn(uriSpec);
        when(uriSpec.uri("/gateway-service/gateway/{gatewayId}/department", gatewayId)).thenReturn(headersSpec);
        when(headersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(String.class)).thenReturn(Mono.just(expectedDeptId));

        String result = gatewayAdapter.getDepartmentIdByGatewayId(gatewayId);

        Assertions.assertEquals("department1", result);
    }
}