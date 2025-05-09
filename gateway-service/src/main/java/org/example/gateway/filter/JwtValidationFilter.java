package org.example.gateway.filter;

import org.apache.http.HttpHeaders;
import org.example.gateway.JwtGenerator;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebExchange;

import lombok.AllArgsConstructor;
import reactor.core.publisher.Mono;

@Component
@AllArgsConstructor
public class JwtValidationFilter implements GlobalFilter {
    private final JwtGenerator jwtGenerator;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String requestPath = exchange.getRequest().getPath().value();

        if (requestPath.startsWith("/api/v1/auth/login")
                || requestPath.startsWith("/login/oauth2/code/google")
                || requestPath.startsWith("/oauth2/authorization/google")
                || requestPath.startsWith("/api/v1/auth/add")
                || requestPath.startsWith("/actuator")) {
            return chain.filter(exchange);
        }

        String authHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return Mono.error(new ResponseStatusException(HttpStatus.UNAUTHORIZED,
                    "Missing or invalid Authorization header"));
        }

        String token = authHeader.substring(7);

        if (!jwtGenerator.validateToken(token)) {
            return Mono.error(new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid JWT token"));
        }

        return chain.filter(exchange);
    }
}