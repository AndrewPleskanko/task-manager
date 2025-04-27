package org.example.gateway.config;

import org.example.gateway.filter.JwtValidationFilter;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import lombok.AllArgsConstructor;

@Configuration
@AllArgsConstructor
public class GatewayConfig {
    private final JwtValidationFilter jwtValidationFilter;

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                .route(r -> r.path("/api/v1/auth/**", "/api/v1/users/**", "/login/oauth2/code/google", "/oauth2/authorization/google")
                        .uri("lb://auth-service")
                )
                .route(r -> r.path("/api/v1/tasks/**")
                        .filters(f -> f.filter(jwtValidationFilter::filter))
                        .uri("lb://task-service")
                )
                .route(r -> r.path("/api/v1/ai/**")
                        .filters(f -> f.filter(jwtValidationFilter::filter))
                        .uri("lb://ai-integration")
                )
                .build();
    }
}
