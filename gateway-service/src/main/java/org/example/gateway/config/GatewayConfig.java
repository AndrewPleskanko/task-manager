package org.example.gateway.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayConfig {

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                .route(r -> r.path("/api/v1/auth/**", "/login/oauth2/code/google", "/oauth2/authorization/google") // Add v1 to the path
                        .uri("lb://auth-service")
                )
                .route(r -> r.path("/api/tasks/**")
                        .uri("lb://task-service")
                )
                .build();
    }
}
