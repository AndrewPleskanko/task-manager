package org.example.gateway.filter;

import org.apache.http.HttpHeaders;
import org.example.gateway.JwtGenerator;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import lombok.AllArgsConstructor;
import reactor.core.publisher.Mono;

@Component
@AllArgsConstructor
public class JwtValidationFilter implements GlobalFilter {
    private final JwtGenerator jwtGenerator;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String authHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return chain.filter(exchange);
        }

        String token = authHeader.substring(7);
        if (!jwtGenerator.validateToken(token)) {
            return chain.filter(exchange);
        }

        Long userId = jwtGenerator.getUserIdFromJwt(token);

        ServerHttpRequest modifiedRequest = exchange.getRequest().mutate()
                .header("X-User-Id", userId.toString())
                .build();
        System.out.println("Modified Request: " + userId);
        return chain.filter(exchange.mutate().request(modifiedRequest).build());
    }
}
