package com.nhnacademy.ruleengineservice.gateway.adapter;

public interface GatewayAdapter {
    String getDepartmentIdByGatewayId(Long gatewayId);
    void activateGateway(Long gatewayId);
}
