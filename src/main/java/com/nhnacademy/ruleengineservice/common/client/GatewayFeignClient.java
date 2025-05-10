package com.nhnacademy.ruleengineservice.common.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "gateway-service", url = "${gateway.service.url}")
public interface GatewayFeignClient {

    @GetMapping("/gateway-service/gateway/{gatewayId}/department")
    String getDepartmentIdByGatewayId(@PathVariable("gatewayId") String gatewayId);

    @GetMapping("/gateway-service/gateway/{gatewayId}/activate")
    void activateGateway(@PathVariable("gatewayId") String gatewayId);
}
