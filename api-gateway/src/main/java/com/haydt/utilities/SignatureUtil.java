package com.haydt.utilities;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.stereotype.Component;

import java.util.Base64;

@Component
public class SignatureUtil {
    private static String signatureSecret = "e0666556231b973798326d87101910eb8ac71c13b1878755b71140579dbd3d56";

    public String generateSignature() throws Exception {

        Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
        SecretKeySpec secretKeySpec = new SecretKeySpec(signatureSecret.getBytes(), "HmacSHA256");
        sha256_HMAC.init(secretKeySpec);

        byte[] hash = sha256_HMAC.doFinal(signatureSecret.getBytes());
        return Base64.getEncoder().encodeToString(hash);
    }

    public String generateSignature(String token) throws Exception {

        Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
        SecretKeySpec secretKeySpec = new SecretKeySpec(token.getBytes(), "HmacSHA256");
        sha256_HMAC.init(secretKeySpec);

        byte[] hash = sha256_HMAC.doFinal(token.getBytes());
        return Base64.getEncoder().encodeToString(hash);
    }
}
