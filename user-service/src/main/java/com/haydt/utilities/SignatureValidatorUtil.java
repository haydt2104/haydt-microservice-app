package com.haydt.utilities;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class SignatureValidatorUtil {
    @Value("${secret.signature}")
    private String SECRET_KEY; // Your secret key
    private static final long ALLOWED_TIME_DIFF = 300000; // 5 minutes in milliseconds

    public boolean isValidSignature(String timestamp, String providedSignature) {
        try {
            long currentTime = System.currentTimeMillis();
            long providedTime = Long.parseLong(timestamp);

            // Kiểm tra nếu thời gian chênh lệch quá lớn
            if (Math.abs(currentTime - providedTime) > ALLOWED_TIME_DIFF) {
                return false;
            }

            // Tạo chữ ký mong đợi
            String expectedSignature = generateSignature(timestamp, SECRET_KEY);

            // So sánh chữ ký
            return expectedSignature.equals(providedSignature);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private String generateSignature(String data, String secretKey) {
        try {
            Mac sha256Hmac = Mac.getInstance("HmacSHA256");
            SecretKeySpec secretKeySpec = new SecretKeySpec(secretKey.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
            sha256Hmac.init(secretKeySpec);

            byte[] hash = sha256Hmac.doFinal(data.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(hash);
        } catch (Exception e) {
            throw new RuntimeException("Failed to generate HMAC-SHA256 signature", e);
        }
    }
}
