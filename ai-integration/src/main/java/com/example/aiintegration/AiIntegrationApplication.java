package com.example.aiintegration;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
public class AiIntegrationApplication {
    public static void main(String[] args) {
        SpringApplication.run(AiIntegrationApplication.class, args);
    }
}
