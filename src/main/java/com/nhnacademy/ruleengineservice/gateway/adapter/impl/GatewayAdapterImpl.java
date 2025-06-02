package com.nhnacademy.ruleengineservice.gateway.adapter.impl;

import com.nhnacademy.ruleengineservice.common.client.GatewayFeignClient;
import com.nhnacademy.ruleengineservice.gateway.adapter.GatewayAdapter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class GatewayAdapterImpl implements GatewayAdapter {

    private final GatewayFeignClient gatewayFeignClient;

    public GatewayAdapterImpl(GatewayFeignClient gatewayFeignClient) {
        this.gatewayFeignClient = gatewayFeignClient;
    }

    @Override
    public String getDepartmentIdByGatewayId(Long gatewayId) {
        try {
            return gatewayFeignClient.getDepartmentIdByGatewayId(gatewayId);
        } catch (Exception e) {
            log.warn("[Feign] 게이트웨이에서 departmentId 조회 실패 - gatewayId: {}", gatewayId);
            return null;
        }
    }

    @Override
    public void activateGateway(Long gatewayId) {
        try {
            gatewayFeignClient.activateGateway(gatewayId);
            log.info("게이트웨이 [{}] 활성화 완료", gatewayId);
        } catch (Exception e) {
            log.warn("게이트웨이 [{}] 활성화 실패", gatewayId);
        }
    }
}
