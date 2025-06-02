package com.nhnacademy.ruleengineservice.gateway.adapter.impl;

import com.nhnacademy.ruleengineservice.common.client.GatewayFeignClient;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class GatewayAdapterImplTest {

    @Mock
    GatewayFeignClient gatewayFeignClient;

    @InjectMocks
    GatewayAdapterImpl gatewayAdapter;

    @Test
    @DisplayName("gatewayId로 departmentId 가져오기 - 성공")
    void getDepartmentIdByGatewayId_success() {
        Long gatewayId = 1L;
        String expectedDeptId = "department1";

        when(gatewayFeignClient.getDepartmentIdByGatewayId(gatewayId)).thenReturn(expectedDeptId);

        String result = gatewayAdapter.getDepartmentIdByGatewayId(gatewayId);

        assertEquals("department1", result);
        verify(gatewayFeignClient).getDepartmentIdByGatewayId(gatewayId);
    }

    @Test
    @DisplayName("gatewayId로 departmentId 가져오기 - 실패")
    void getDepartmentIdByGatewayId_fail() {
        Long gatewayId = 1L;

        when(gatewayFeignClient.getDepartmentIdByGatewayId(gatewayId)).thenThrow(new RuntimeException("Feign error"));

        String result = gatewayAdapter.getDepartmentIdByGatewayId(gatewayId);

        assertNull(result);
        verify(gatewayFeignClient).getDepartmentIdByGatewayId(gatewayId);
    }

    @Test
    @DisplayName("gatewayId로 게이트웨이 활성화 성공")
    void activateGateway_success() {
        Long gatewayId = 1L;

        gatewayAdapter.activateGateway(gatewayId);

        verify(gatewayFeignClient).activateGateway(gatewayId);
    }

    @Test
    @DisplayName("gatewayId로 게이트웨이 활성화 실패")
    void activateGateway_fail() {
        Long gatewayId = 1L;

        doThrow(new RuntimeException("Feign error")).when(gatewayFeignClient).activateGateway(gatewayId);

        gatewayAdapter.activateGateway(gatewayId);

        verify(gatewayFeignClient).activateGateway(gatewayId);
    }
}