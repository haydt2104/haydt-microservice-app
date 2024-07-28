package com.haydt.filters;

import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;

import com.haydt.utilities.JwtUtil;
import com.haydt.utilities.SignatureUtil;

import reactor.core.publisher.Mono;

import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.lang.NonNull;

import java.util.Optional;

@Component
public class JwtAuthenticationFilter implements WebFilter {

    @Override
    public @NonNull Mono<Void> filter(@NonNull ServerWebExchange exchange, @NonNull WebFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        SignatureUtil signatureUtil = new SignatureUtil();
        System.out.println("Filtering request to: " + request.getURI().getPath());

        String token = getTokenFromCookie(request).orElse(null);

        if (token == null) {
            ServerHttpRequest modifiedRequest;
            try {
                modifiedRequest = request.mutate()
                        .header("X-Signature", signatureUtil.generateSignature())
                        .build();
                ServerWebExchange modifiedExchange = exchange.mutate()
                        .request(modifiedRequest)
                        .build();

                return chain.filter(modifiedExchange);
            } catch (Exception e) {
                // TODO Auto-generated catch block
                return onError(exchange, "Error generating signature", HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

        try {
            String userID = JwtUtil.extractUsername(token);
            // Thêm header với thông tin đã xác thực
            ServerHttpRequest modifiedRequest = request.mutate()
                    .header("X-Authenticated-User", userID)
                    .header("X-Signature", signatureUtil.generateSignature(token))
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
        return Optional.ofNullable(request.getCookies().getFirst("access_token"))
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
