package com.haydt.filters;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

@Component
public class SignatureHeaderFilter extends AbstractGatewayFilterFactory<SignatureHeaderFilter.Config> {

    private static final String SECRET_KEY = "0da5b6fe0595f95d31df2920b5a67f470e8f114c28ab07745cd14c2af039ff3152b1221434767a7f542a44ecf92fa30d72d9ea02ccd09364c6450503e05f3ba1";

    public SignatureHeaderFilter() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();

            // Lấy thời gian hiện tại
            String timestamp = String.valueOf(System.currentTimeMillis());

            // Tạo signature
            String signature = generateSignature(timestamp, SECRET_KEY);

            // Thêm signature và timestamp vào header
            ServerHttpRequest modifiedRequest = request.mutate()
                    .header("X-API-GATEWAY-SIGNATURE", signature)
                    .header("X-TIMESTAMP", timestamp)
                    .build();

            return chain.filter(exchange.mutate().request(modifiedRequest).build());
        };
    }

    private String generateSignature(String data, String secretKey) {
        try {
            Mac sha256Hmac = Mac.getInstance("HmacSHA256");
            SecretKeySpec secretKeySpec = new SecretKeySpec(secretKey.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
            sha256Hmac.init(secretKeySpec);

            byte[] hash = sha256Hmac.doFinal(data.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(hash); // Encode the hash using Base64
        } catch (Exception e) {
            throw new RuntimeException("Failed to generate HMAC-SHA256 signature", e);
        }
    }

    // Lớp Config dùng để cấu hình cho filter, có thể mở rộng nếu cần thiết
    public static class Config {
        // Thêm các tham số cấu hình nếu cần, ví dụ:
        // private boolean shouldAddSignature;

        // Getters và Setters nếu cần cấu hình
    }
}