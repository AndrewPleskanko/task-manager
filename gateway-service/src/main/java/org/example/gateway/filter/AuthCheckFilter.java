package org.example.gateway.filter;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ServerWebExchange;

import reactor.core.publisher.Mono;

public class AuthCheckFilter implements GatewayFilter, Ordered {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String token = exchange.getRequest().getHeaders().getFirst("Authorization");

        // Ваша логіка перевірки токена аутентифікації тут
        if (isValidToken(token)) {
            return chain.filter(exchange);
        } else {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }
    }

    private boolean isValidToken(String token) {
        // Ваша логіка перевірки токена. Наприклад, перевірка валідності токена JWT
        return token != null && token.startsWith("Bearer ");
    }

    @Override
    public int getOrder() {
        return 1;
    }
}

