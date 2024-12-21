package org.example.gateway.config;

import org.example.gateway.filter.AuthCheckFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayConfig {

    @Bean
    public AuthCheckFilter authCheckFilter() {
        return new AuthCheckFilter();
    }
}

