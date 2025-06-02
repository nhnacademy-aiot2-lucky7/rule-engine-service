package com.nhnacademy.ruleengineservice.common.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "gateway-service", url = "${gateway.service.url}")
public interface GatewayFeignClient {

    @GetMapping("/gateways/{gateway-id}/department-id")
    String getDepartmentIdByGatewayId(@PathVariable("gateway-id") String gatewayId);

    @GetMapping("/gateway-service/gateway/{gateway-id}/activate")
    void activateGateway(@PathVariable("gateway-id") String gatewayId);
}
