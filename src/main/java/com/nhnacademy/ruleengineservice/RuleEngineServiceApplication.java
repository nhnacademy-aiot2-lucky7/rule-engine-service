package com.nhnacademy.ruleengineservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients(basePackages = "com.nhnacademy.ruleengineservice")
public class RuleEngineServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(RuleEngineServiceApplication.class, args);
    }

}
