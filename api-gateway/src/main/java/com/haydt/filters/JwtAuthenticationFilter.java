package com.haydt.filters;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;

import com.haydt.utilities.SignatureUtil;

import org.springframework.http.HttpCookie;

@Component
public class JwtAuthenticationFilter implements GlobalFilter, Ordered {

    // Tạo SecretKey cho HMAC
    private static final SecretKey SECRET_KEY = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();

        // Lấy JWT từ Cookie
        HttpCookie jwtCookie = request.getCookies().getFirst("jwt_token");

        if (jwtCookie == null) {
            return onError(exchange, "JWT token is missing", HttpStatus.UNAUTHORIZED);
        }

        String token = jwtCookie.getValue();

        try {
            // Giải mã JWT
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(SECRET_KEY)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            String userId = claims.getSubject();
            String userRole = claims.get("role", String.class);

            // Tạo chữ ký cho request
            String signature = SignatureUtil.generateSignature(token);

            // Thêm header với thông tin đã xác thực
            ServerHttpRequest modifiedRequest = exchange.getRequest().mutate()
                    .header("X-Authenticated-User", userId)
                    .header("X-User-Role", userRole)
                    .header("X-Signature", signature)
                    .build();

            ServerWebExchange modifiedExchange = exchange.mutate()
                    .request(modifiedRequest)
                    .build();

            return chain.filter(modifiedExchange);

        } catch (Exception e) {
            return onError(exchange, "Authorization token is invalid", HttpStatus.UNAUTHORIZED);
        }
    }

    private Mono<Void> onError(ServerWebExchange exchange, String err, HttpStatus httpStatus) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(httpStatus);

        return response.setComplete();
    }

    @Override
    public int getOrder() {
        return -1; // Đặt thứ tự của filter
    }
}
