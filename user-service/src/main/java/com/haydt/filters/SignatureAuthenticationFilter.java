package com.haydt.filters;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.haydt.utilities.SignatureValidatorUtil;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
public class SignatureAuthenticationFilter extends OncePerRequestFilter {
    @Autowired
    private SignatureValidatorUtil signatureValidatorUtil;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain) throws ServletException, IOException {
        final String signatureHeader = request.getHeader("X-API-GATEWAY-SIGNATURE");
        final String timestampHeaderValue = request.getHeader("X-TIMESTAMP");

        System.out.println("Filtering request to: " + request.getRequestURI());
        System.out.println("Signature: " + signatureHeader);
        System.out.println("Timestamp: " + timestampHeaderValue);
        Map<String, String> responseBody = new HashMap<>();
        ObjectMapper objectMapper = new ObjectMapper();

        if (signatureHeader == null || timestampHeaderValue == null) {

            responseBody.put("error", "Unauthorized");
            responseBody.put("message", "Missing signature or timestamp");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            response.getWriter().write(objectMapper.writeValueAsString(responseBody));
            return;
        }

        if (!signatureValidatorUtil.isValidSignature(timestampHeaderValue, signatureHeader)) {
            responseBody.put("error", "Unauthorized");
            responseBody.put("message", "Invalid signature");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            response.getWriter().write(objectMapper.writeValueAsString(responseBody));
            return;
        }
        filterChain.doFilter(request, response);
    }
}