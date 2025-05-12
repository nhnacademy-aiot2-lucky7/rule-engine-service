package com.nhnacademy.ruleengineservice.gateway.adapter.impl;

import com.nhnacademy.ruleengineservice.common.client.GatewayFeignClient;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class GatewayAdapterImplTest {

    @Mock
    GatewayFeignClient gatewayFeignClient;

    @InjectMocks
    GatewayAdapterImpl gatewayAdapter;

    @Test
    @DisplayName("gatewayId로 departmentId 가져오기 - 성공")
    void getDepartmentIdByGatewayId_successed() {
        String gatewayId = "gateway1";
        String expectedDeptId = "department1";

        when(gatewayFeignClient.getDepartmentIdByGatewayId(gatewayId)).thenReturn(expectedDeptId);

        String result = gatewayAdapter.getDepartmentIdByGatewayId(gatewayId);

        Assertions.assertEquals("department1", result);
    }
}