package com.nhnacademy.ruleengineservice.gateway.adapter;

public interface GatewayAdapter {
    String getDepartmentIdByGatewayId(String gatewayId);
    void activateGateway(String gatewayId);
}
