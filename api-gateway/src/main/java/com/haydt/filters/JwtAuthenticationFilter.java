package com.haydt.filters;

import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;

import com.haydt.utilities.SignatureUtil;

import reactor.core.publisher.Mono;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.lang.NonNull;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.util.Optional;

@Component
public class JwtAuthenticationFilter implements WebFilter {

    // Tạo SecretKey cho HMAC
    private static final SecretKey SECRET_KEY = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    @Override
    public @NonNull Mono<Void> filter(@NonNull ServerWebExchange exchange, @NonNull WebFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();

        // Bỏ qua các yêu cầu đến các đường dẫn cụ thể
        if (request.getURI().getPath().startsWith("/user/auth/")
                || request.getURI().getPath().startsWith("/user/public/")) {
            return chain.filter(exchange); // Không áp dụng bộ lọc cho các đường dẫn này
        }

        // Lấy JWT từ Cookie
        String token = getTokenFromCookie(request).orElse(null);

        if (token == null) {
            return onError(exchange, "JWT token is missing", HttpStatus.UNAUTHORIZED);
        }

        try {
            // Giải mã JWT
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(SECRET_KEY)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            String userId = claims.getSubject();
            String userRole = claims.get("role", String.class);

            // Tạo chữ ký cho request (nếu cần)
            String signature = SignatureUtil.generateSignature(token);

            // Thêm header với thông tin đã xác thực
            ServerHttpRequest modifiedRequest = request.mutate()
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

    private Optional<String> getTokenFromCookie(ServerHttpRequest request) {
        return Optional.ofNullable(request.getCookies().getFirst("jwt_token"))
                .map(cookie -> cookie.getValue());
    }

    private Mono<Void> onError(ServerWebExchange exchange, String err, HttpStatus httpStatus) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(httpStatus);
        response.getHeaders().add("Content-Type", "application/json");
        response.getHeaders().add("X-Error-Message", err);

        return response.setComplete();
    }
}
