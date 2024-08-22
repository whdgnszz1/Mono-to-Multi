package com.jh.gatewayservice.filter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.Map;

@Component
@Slf4j
public class JwtFilter extends AbstractGatewayFilterFactory<JwtFilter.Config> {

    @Value("${jwt.secret.key}")
    private String jwtSecret;

    public JwtFilter() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();
            ServerHttpResponse response = exchange.getResponse();

            if (!request.getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
                return chain.filter(exchange);
            }

            String authHeader = request.getHeaders().getOrEmpty(HttpHeaders.AUTHORIZATION).get(0);
            if (!authHeader.startsWith("Bearer ")) {
                response.setStatusCode(HttpStatus.UNAUTHORIZED);
                return response.setComplete();
            }

            String token = authHeader.substring(7);

            try {
                Claims claims = Jwts.parser()
                        .setSigningKey(jwtSecret)
                        .parseClaimsJws(token)
                        .getBody();

                log.info("JWT Claims: ");
                ServerHttpRequest.Builder mutatedRequest = request.mutate();
                for (Map.Entry<String, Object> entry : claims.entrySet()) {
                    String claimKey = "X-Claim-" + entry.getKey();
                    String claimValue = String.valueOf(entry.getValue());
                    mutatedRequest.header(claimKey, claimValue);
                    log.info("{}: {}", claimKey, claimValue);
                }

                request = mutatedRequest.build();
                exchange = exchange.mutate().request(request).build();

            } catch (Exception e) {
                log.error("JWT validation failed", e);
                response.setStatusCode(HttpStatus.UNAUTHORIZED);

                return response.setComplete();
            }

            log.info("Custom PRE filter: request uri -> {}", request.getURI());
            log.info("Custom PRE filter: request id -> {}", request.getId());

            return chain.filter(exchange).then(Mono.fromRunnable(() -> {
                log.info("Custom POST filter: response status code -> {}", response.getStatusCode());
            }));
        };
    }

    @Data
    public static class Config {
        private boolean preLogger;
        private boolean postLogger;
    }
}
